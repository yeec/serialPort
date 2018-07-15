package bros.manage.business.service;

import java.util.EventListener;

import bros.manage.event.SPTelegramEvent;

public interface SPTelegramListener extends EventListener {
	public void DealTelegramEvent(SPTelegramEvent telegramEvent);
}