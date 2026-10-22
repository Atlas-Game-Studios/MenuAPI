package com.ags.menuapi.dialog;

import com.ags.atlaslib.util.MessageUtil;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Shared helpers for Paper dialogs (the screens to use instead of a chest menu for text, inputs and confirmations).
 * Labels, tooltips and body lines are MiniMessage strings.
 */
public final class Dialogs {

    private static final ClickCallback.Options ONCE = ClickCallback.Options.builder().uses(1).build();

    private Dialogs() {
    }

    /** A button that runs onClick for the player who pressed it. tooltip may be null. */
    public static ActionButton button(String label, String tooltip, int width, Consumer<Player> onClick) {
        return button(label, tooltip, width, (view, player) -> onClick.accept(player));
    }

    /** A button that also gets the dialog's input values (view.getText("key"), ...). tooltip may be null. */
    public static ActionButton button(String label, String tooltip, int width, BiConsumer<DialogResponseView, Player> onClick) {
        ActionButton.Builder builder = ActionButton.builder(MessageUtil.convertMsg(label))
                .width(width)
                .action(DialogAction.customClick((view, audience) -> {
                    if (audience instanceof Player player) onClick.accept(view, player);
                }, ONCE));
        if (tooltip != null) builder.tooltip(MessageUtil.convertMsg(tooltip));
        return builder.build();
    }

    /** A button with a ready-made label (for example Drawn.onPlaque). tooltip may be null. */
    public static ActionButton button(Component label, Component tooltip, int width, Consumer<Player> onClick) {
        ActionButton.Builder builder = ActionButton.builder(label)
                .width(width)
                .action(DialogAction.customClick((response, audience) -> {
                    if (audience instanceof Player player) onClick.accept(player);
                }, ONCE));
        if (tooltip != null) builder.tooltip(tooltip);
        return builder.build();
    }

    /** Body paragraphs, one per line. */
    public static List<DialogBody> text(List<String> lines) {
        return lines.stream().map(line -> (DialogBody) DialogBody.plainMessage(MessageUtil.convertMsg(line), 300)).toList();
    }

    /** Buttons in columns with an exit button under them (Escape also closes). body may be empty. */
    public static void menu(Player player, Component title, List<? extends DialogBody> body, List<ActionButton> buttons,
                            ActionButton exit, int columns) {
        player.showDialog(Dialog.create(factory -> factory.empty()
                .base(DialogBase.builder(title).canCloseWithEscape(true).body(body).build())
                .type(DialogType.multiAction(buttons, exit, columns))));
    }

    /** Read-only dialog with one button; a null button is the client's plain OK. */
    public static void notice(Player player, Component title, List<? extends DialogBody> body, ActionButton button) {
        player.showDialog(Dialog.create(factory -> factory.empty()
                .base(DialogBase.builder(title).canCloseWithEscape(true).body(body).build())
                .type(button == null ? DialogType.notice() : DialogType.notice(button))));
    }

    /**
     * One-line text prompt with Confirm/Cancel. onConfirm gets the text and returns null when done, or the text to
     * reopen the prompt with (for example a corrected amount). Cancel and Escape just close.
     */
    public static void textInput(Player player, String title, String initial, BiFunction<Player, String, String> onConfirm) {
        Dialog dialog = Dialog.create(factory -> factory.empty()
                .base(DialogBase.builder(MessageUtil.convertMsg(title))
                        .canCloseWithEscape(true)
                        .inputs(List.of(DialogInput.text("text", Component.empty()).initial(initial).labelVisible(false).build()))
                        .build())
                .type(DialogType.confirmation(
                        button("<green>Confirm", null, 150, (view, p) -> {
                            String retry = onConfirm.apply(p, view.getText("text"));
                            if (retry != null) textInput(p, title, retry, onConfirm);
                        }),
                        button("<gray>Cancel", null, 150, p -> {
                        }))));
        player.showDialog(dialog);
    }

    /** Yes/Cancel dialog. Cancel runs onCancel; Escape just closes. */
    public static void confirm(Player player, String title, List<String> body, String yesLabel,
                               Consumer<Player> onYes, Consumer<Player> onCancel) {
        Dialog dialog = Dialog.create(factory -> factory.empty()
                .base(DialogBase.builder(MessageUtil.convertMsg(title))
                        .canCloseWithEscape(true)
                        .body(text(body))
                        .build())
                .type(DialogType.confirmation(
                        button(yesLabel, null, 150, onYes),
                        button("<gray>Cancel", null, 150, onCancel))));
        player.showDialog(dialog);
    }
}
