package telegram;

import ExchangerateService.ExchangeRateService;
import GraphService.TradingViewScreenshotService;
import configUtil.ConfigUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllGroupChats;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TelegramBotApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = ConfigUtil.get("telegram.bot.name"); // имя бота
    public static final String TELEGRAM_BOT_TOKEN = ConfigUtil.get("telegram.bot.token"); // токен бота
    ExchangeRateService exchangerateService = new ExchangeRateService();
    Long chatId;

    public TelegramBotApp() throws TelegramApiException {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
        List<BotCommand> commands = Arrays.stream(CurrencyEnum.values())
                .map(c -> new BotCommand(c.getCommand(), c.getLabel()))
                .collect(Collectors.toList());

        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setCommands(commands);
        setMyCommands.setScope(new BotCommandScopeAllGroupChats()); // или GroupChat, если хочешь только для групп
        execute(setMyCommands);
        WebDriverManager.chromedriver().setup();
        //TODO: Подумать как отправлять сообщение в чат при добавлении бота в группу для активации scheduleDailyMessage()
        //sendTextMessage("Привет, я твой финансовый бот!\nНажми команду /start, чтобы активировать бота!");

    }

    @Override
    public void onUpdateEventReceived(Update update) throws IOException, InterruptedException {

        boolean isStartMessage = false;

        //TODO: Вызов метода формирования графика и отправка в чат вместе с сообщением
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            chatId = update.getMessage().getChatId(); // Получаем ID чата (группы или личного чата)
            String text = message.getText();

            if (text.contains("@")) {
                String[] parts = text.split("@");
                if (!parts[1].equalsIgnoreCase(TELEGRAM_BOT_NAME)) {
                    return; // не наш бот — игнорим
                }
            }

            String command = text.split("@")[0];

            if (command.equals("/start")) {
                isStartMessage = true;
                sendTextMessage(chatId,"Бот активирован!");
                scheduleDailyMessage();
            }

            if (command.startsWith("/") && !isStartMessage) {
                String currencyCode = command.substring(1).toUpperCase();

                if (currencyCode.isEmpty()) {
                    return;
                }

                String result = exchangerateService.getExchangeRate(currencyCode, "RUB");

                TradingViewScreenshotService chartService = new TradingViewScreenshotService();
                InputFile chart = chartService.captureChart(currencyCode);

                if (chart != null) {
                    sendPhotoMessage(chatId, chart, result);
                } else {
                    sendTextMessage(chatId, result); // fallback если нет скриншота графика
                }
            }
        }
    }

    private void scheduleDailyMessage() {
        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        LocalTime targetTime = LocalTime.of(12, 30);
        ZonedDateTime now = ZonedDateTime.now(moscowZone);
        ZonedDateTime nextRun = now.with(targetTime);
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1);
        }

        long initialDelay = Duration.between(now, nextRun).toMillis();
        long dayInMillis = 24 * 60 * 60 * 1000;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    String message = exchangerateService.getExchangeRate("USD", "RUB");
                    sendTextMessage(chatId, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, initialDelay, dayInMillis);
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TelegramBotApp());
    }
}
