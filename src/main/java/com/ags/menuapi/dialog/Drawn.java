package com.ags.menuapi.dialog;

import com.ags.atlaslib.util.MessageUtil;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.object.ObjectContents;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pieces for drawn dialogs: glyphs from the resource pack's atlas:dialog font (built by
 * models/gui/build_dialog_glyphs.py), pixel shifts, inline sprites, and labels laid over plaque art.
 *
 * What the client does (tested with /menuapi drawn): body glyphs are not clipped and later body lines draw
 * over them; button labels are clipped to their button; labels are centred as a whole.
 */
public final class Drawn {

    public static final Key FONT = Key.key("atlas", "dialog");

    /** Plaque art the size of a dialog button (or the title bar). Chars and sizes match the pack. */
    public enum Plaque {
        SAGE('\uE001', 150), LOCKED('\uE003', 150), TITLE('\uE004', 200);

        final char glyph;
        public final int width;

        Plaque(char glyph, int width) {
            this.glyph = glyph;
            this.width = width;
        }
    }

    /** 300x60 framed panel in two 150 px halves (glyphs must fit a 256 px font page). */
    private static final char SUMMARY_LEFT = '\uE005', SUMMARY_RIGHT = '\uE006';
    public static final char WELL = '\uE002';

    // advance of each printable ASCII char in the vanilla font (glyph width + 1), from ascii.png
    private static final int[] ADVANCE = {4, 2, 4, 6, 6, 6, 6, 2, 4, 4, 4, 6, 2, 6, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 5, 6, 5, 6, 7, 6,
            6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 4, 6, 6, 3, 6, 6, 6, 6, 6, 5, 6, 6, 2, 6, 5, 3, 6,
            6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 4, 2, 4, 7};
    /** Inline sprites and heads draw 8 px wide. */
    public static final int SPRITE_WIDTH = 9;

    private Drawn() {
    }

    public static Component glyph(char c) {
        return Component.text(c).font(FONT);
    }

    /** Moves the pen by px pixels (negative = left), using the font's power-of-two space table. */
    public static Component shift(int px) {
        if (px == 0) return Component.empty();
        StringBuilder sb = new StringBuilder();
        int base = px < 0 ? 0xF001 : 0xF101;
        int n = Math.abs(px);
        for (int i = 8; i >= 0; i--) {
            while (n >= (1 << i)) {
                sb.append((char) (base + i));
                n -= 1 << i;
            }
        }
        return Component.text(sb.toString()).font(FONT);
    }

    /** An inline atlas sprite, e.g. sprite("minecraft:items", "item/saddle"). */
    public static Component sprite(String atlas, String sprite) {
        return Component.object(ObjectContents.sprite(Key.key(atlas), Key.key(sprite)));
    }

    /** Width in pixels of MiniMessage text in the default font (non-ASCII counts as 6; bold adds 1 per char). */
    public static int width(String miniMessage) {
        String plain = MiniMessage.miniMessage().stripTags(miniMessage);
        boolean bold = miniMessage.contains("<b>") || miniMessage.contains("<bold>");
        int w = 0;
        for (char c : plain.toCharArray()) {
            w += (c >= 32 && c < 127 ? ADVANCE[c - 32] : 6) + (bold ? 1 : 0);
        }
        return w;
    }

    /**
     * Label for a button exactly plaque.width wide: the plaque covers the button and the content is centred
     * on it. contentWidth is the content's width in pixels (see width(), plus SPRITE_WIDTH per sprite).
     */
    public static Component onPlaque(Plaque plaque, Component content, int contentWidth) {
        int pad = Math.max(0, (plaque.width - contentWidth) / 2);
        // children keep their own fonts because the root carries no style
        return Component.textOfChildren(glyph(plaque.glyph), shift(-(plaque.width + 1) + pad), content,
                shift(plaque.width - pad - contentWidth));
    }

    public static Component onPlaque(Plaque plaque, String miniMessage) {
        return onPlaque(plaque, MessageUtil.convertMsg(miniMessage), width(miniMessage));
    }

    /** A dialog title on the wood title plaque. */
    public static Component title(String miniMessage) {
        return onPlaque(Plaque.TITLE, miniMessage);
    }

    // ------------------------------------------------------------------ card wall

    /** Card frame art (96x72, 8 text lines tall). The pack draws previews 4 px inside it. */
    public enum CardFrame {
        WOOD('\uE007'), SAGE('\uE008'), LOCKED('\uE009');

        final char glyph;

        CardFrame(char glyph) {
            this.glyph = glyph;
        }
    }

    private static final char CARD_VEIL = '\uE00A';
    private static final int CARD_W = 96, CARD_GAP = 6, CARD_LINES = 8, PREVIEW_W = 88, ROW_W = 300;

    /**
     * One card: preview is the font holding the cosmetic's preview glyph (U+E000), e.g.
     * atlas:preview/mount/rex. The whole card (art, the lines under it, the name) runs onClick.
     */
    public record Card(Key preview, CardFrame frame, String nameMiniMessage, Component hover, Consumer<Player> onClick) {
    }

    /** Cards three to a row; each row is one body element. Rows are left-aligned on a 300 px grid. */
    public static List<DialogBody> cardWall(List<Card> cards) {
        List<DialogBody> rows = new ArrayList<>();
        for (int i = 0; i < cards.size(); i += 3) {
            // the body wraps text a few px short of its width (padding), and a wrapped spacer line doubles the row
            rows.add(DialogBody.plainMessage(cardRow(cards.subList(i, Math.min(i + 3, cards.size()))), ROW_W + 20));
        }
        return rows;
    }

    private static Component cardRow(List<Card> row) {
        List<Style> styles = new ArrayList<>();
        for (Card card : row) {
            ClickEvent click = ClickEvent.callback(audience -> {
                if (audience instanceof Player player) card.onClick().accept(player);
            }, ClickCallback.Options.builder().uses(1).lifetime(Duration.ofMinutes(10)).build());
            styles.add(Style.style().clickEvent(click).hoverEvent(HoverEvent.showText(card.hover())).build());
        }
        int used = row.size() * CARD_W + (row.size() - 1) * CARD_GAP;
        List<Component> lines = new ArrayList<>();
        // line 1: frame, preview (and the locked veil) drawn from the line top; they overhang the next 7 lines
        lines.add(joinRow(row, styles, used, card -> Component.textOfChildren(
                glyph(card.frame().glyph), shift(-(CARD_W + 1) + 4),
                Component.text('\uE000').font(card.preview()),
                card.frame() == CardFrame.LOCKED ? Component.textOfChildren(shift(-(PREVIEW_W + 1)), glyph(CARD_VEIL)) : Component.empty(),
                shift(CARD_W - 4 - (PREVIEW_W + 1)))));
        // lines 2-8: invisible spacers so the whole card area is clickable
        for (int i = 1; i < CARD_LINES; i++) lines.add(joinRow(row, styles, used, card -> shift(CARD_W)));
        // line 9: the name, centred under its card and cut to fit
        lines.add(joinRow(row, styles, used, card -> {
            String name = fit(card.nameMiniMessage(), CARD_W - 4);
            int w = width(name), pad = (CARD_W - w) / 2;
            return Component.textOfChildren(shift(pad), MessageUtil.convertMsg(name), shift(CARD_W - pad - w));
        }));
        Component out = Component.empty();
        for (int i = 0; i < lines.size(); i++) {
            out = out.append(lines.get(i));
            if (i < lines.size() - 1) out = out.append(Component.newline());
        }
        return out;
    }

    private static Component joinRow(List<Card> row, List<Style> styles, int used, Function<Card, Component> part) {
        Component line = Component.empty();
        for (int i = 0; i < row.size(); i++) {
            if (i > 0) line = line.append(shift(CARD_GAP));
            line = line.append(Component.textOfChildren(part.apply(row.get(i))).style(styles.get(i)));
        }
        return line.append(shift(ROW_W - used));
    }

    private static final Pattern LEADING_TAGS = Pattern.compile("^(<[^>]+>)*");

    /** Cuts MiniMessage text (keeping its leading tags) with "..." until it fits maxWidth pixels. */
    static String fit(String miniMessage, int maxWidth) {
        if (width(miniMessage) <= maxWidth) return miniMessage;
        Matcher tags = LEADING_TAGS.matcher(miniMessage);
        String prefix = tags.find() ? tags.group() : "";
        String plain = MiniMessage.miniMessage().stripTags(miniMessage.substring(prefix.length()));
        while (!plain.isEmpty() && width(prefix + plain + "...") > maxWidth) plain = plain.substring(0, plain.length() - 1);
        return prefix + plain.stripTrailing() + "...";
    }

    /** First line of a summary block: the 300x60 panel, 3 px above the line top. Put the text lines after it in the same body element, width >= 320. */
    public static Component summaryPanel() {
        return Component.textOfChildren(glyph(SUMMARY_LEFT), shift(-1), glyph(SUMMARY_RIGHT));
    }

    /** Lines padded to the widest one, so a centred multi-line message reads left-aligned. Lines are MiniMessage. */
    public static Component leftAligned(List<String> lines) {
        int max = lines.stream().mapToInt(Drawn::width).max().orElse(0);
        Component out = Component.empty();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            out = out.append(Component.textOfChildren(MessageUtil.convertMsg(line), shift(max - width(line))));
            if (i < lines.size() - 1) out = out.append(Component.newline());
        }
        return out;
    }
}
