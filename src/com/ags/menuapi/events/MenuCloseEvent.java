package com.ags.menuapi.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.ags.menuapi.Menu.Menu;
import com.ags.menuapi.Menu.MenuPage;

import org.bukkit.entity.Player;

public final class MenuCloseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    
    private Player player;
    private Menu menu;
    private MenuPage page;
    private int pageNumber;
    

    public MenuCloseEvent(Player player, MenuPage page) {
       this.player = player;
       this.menu = page.getHolder();
       this.page = page;
       this.pageNumber = page.getPagenumber();
    }

	public Player getPlayer() {
		return player;
	}

	public Menu getMenu() {
		return menu;
	}

	public MenuPage getPage() {
		return page;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
