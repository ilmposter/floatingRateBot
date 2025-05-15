package telegram;

public enum CurrencyEnum {
    START("/start", "Старт бота"),
    USD("/usd", "💵 USD — 🇺🇸 Доллар США"),
    EUR("/eur", "💶 EUR — 🇪🇺 Евро"),
    RUB("/rub", "🇷🇺 RUB — Российский рубль"),
    GBP("/gbp", "💷 GBP — 🇬🇧 Британский фунт"),
    JPY("/jpy", "💴 JPY — 🇯🇵 Японская иена"),
    CNY("/cny", "🀄 CNY — 🇨🇳 Китайский юань"),
    TRY("/try", "🇹🇷 TRY — Турецкая лира"),
    KZT("/kzt", "🇰🇿 KZT — Казахстанский тенге"),
    UAH("/uah", "🇺🇦 UAH — Украинская гривна"),
    BYN("/byn", "🇧🇾 BYN — Белорусский рубль"),
    CHF("/chf", "🇨🇭 CHF — Швейцарский франк"),
    CAD("/cad", "🇨🇦 CAD — Канадский доллар"),
    AUD("/aud", "🇦🇺 AUD — Австралийский доллар"),
    SEK("/sek", "🇸🇪 SEK — Шведская крона"),
    NOK("/nok", "🇳🇴 NOK — Норвежская крона"),
    PLN("/pln", "🇵🇱 PLN — Польский злотый"),
    CZK("/czk", "🇨🇿 CZK — Чешская крона"),
    INR("/inr", "🇮🇳 INR — Индийская рупия"),
    BRL("/brl", "🇧🇷 BRL — Бразильский реал"),
    ZAR("/zar", "🇿🇦 ZAR — Южноафриканский рэнд"),
    AED("/aed", "🇦🇪 AED — Дирхам ОАЭ"),
    HKD("/hkd", "🇭🇰 HKD — Гонконгский доллар"),
    SGD("/sgd", "🇸🇬 SGD — Сингапурский доллар"),
    MXN("/mxn", "🇲🇽 MXN — Мексиканское песо"),
    KRW("/krw", "🇰🇷 KRW — Южнокорейская вона");

    private final String command;
    private final String label;

    CurrencyEnum(String command, String label) {
        this.command = command;
        this.label = label;
    }

    public String getCommand() {
        return command;
    }

    public String getLabel() {
        return label;
    }

    public static CurrencyEnum fromCommand(String command) {
        for (CurrencyEnum currency : values()) {
            if (currency.toString().equals(command)) {
                return currency;
            }
        }
        return null;
    }
}
