import lombok.Getter;
import lombok.val;
import lombok.var;
import xyz.smsverification.SMSVerification;
import xyz.smsverification.exception.WaitingForMessagesException;

import java.util.Scanner;

@Getter
public class TestSession {
    private String lastOrderId;
    private final SMSVerification smsVerification;

    public TestSession(String apiKey) {
        smsVerification = new SMSVerification(apiKey);
        smsVerification.getHttpUtil().setDebug(true);

        yes();
    }

    public void yes() {
        new Thread(() -> {
            val scanner = new Scanner(System.in);

            while (true) {
                System.out.print(ANSI_CODES.ANSI_BLUE + "Enter the command: " + ANSI_CODES.ANSI_RESET);

                val input = scanner.nextLine();

                try {
                    handleCommands(input);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }).start();
    }

    private void handleCommands(String cmd) throws Exception {
        if (cmd.equalsIgnoreCase("stop")) {
            System.out.println("Shutting down...");
            System.exit(0);
            return;
        }

        if (cmd.equalsIgnoreCase("bal")) {

            val bal = smsVerification.getBalance();
            logBig("Your balance: " + bal + "!");

            return;
        }

        if (cmd.equalsIgnoreCase("price")) {
            var country = "Ukraine";
            var service = "Google";

            enterCmd("-> Enter country and service (space separated) (Leave empty for defaults: " + country + " " + service + "): ");

            val in = new Scanner(System.in).nextLine();
            if (in.length() > 0) {
                country = in.split(" ")[0];
                service = in.split(" ")[1];
            }

            val price = smsVerification.getPrice(country, service);


            logBig("Price of " + country + " " + service + " is: " + price);

            return;
        }

        if (cmd.equalsIgnoreCase("order")) {
            var country = "Ukraine";
            var service = "Google";

            enterCmd("-> Enter country and service (space separated) (Leave empty for defaults: " + country + " " + service + ")" + ": ");

            val in = new Scanner(System.in).nextLine();
            if (in.length() > 0) {
                country = in.split(" ")[0];
                service = in.split(" ")[1];
            }

            val order = smsVerification.order(country, service);

            this.lastOrderId = order.getId();

            logBig("Order ID: " + order.getId() + "\nOrdered Number: " + order.getNumber() + "\nBal change: " + order.getAccountBalanceChange().toString());


            return;
        }

        if (cmd.equalsIgnoreCase("cancel")) {
            var orderId = lastOrderId;

            enterCmd("-> Enter order id " + (orderId != null ? "(Leave empty for last order: " + orderId + ")" : "") + ": ");

            val in = new Scanner(System.in).nextLine();
            if (in.length() > 0) {
                orderId = in;
            }

            val balanceChange = smsVerification.cancelOrder(orderId);

            logBig("Cancelled order with id: " + orderId + "! Bal change: " + balanceChange.toString());

            return;
        }

        if (cmd.equalsIgnoreCase("sent")) {
            var orderId = lastOrderId;

            enterCmd("-> Enter order id " + (orderId != null ? "(Leave empty for last order: " + orderId + ")" : "") + ": ");

            val in = new Scanner(System.in).nextLine();
            if (in.length() > 0) {
                orderId = in;
            }

            val statusChange = smsVerification.markSent(orderId);

            logBig("Marked order with id " + orderId + " as " + statusChange.getNewStatus() + "!");

            return;
        }

        if (cmd.equalsIgnoreCase("check")) {
            var orderId = lastOrderId;

            enterCmd("-> Enter order id " + (orderId != null ? "(Leave empty for last order: " + orderId + ")" : "") + ": ");

            val in = new Scanner(System.in).nextLine();
            if (in.length() > 0) {
                orderId = in;
            }

            try {
                val orderComplete = smsVerification.check(orderId);

                logBig("Order completed! SMS Code: " + orderComplete.getSms());
            } catch (WaitingForMessagesException e) {
                logBig("Nothing new, retry in 10 seconds!");
            }

            return;
        }
    }

    private void logBig(String msg) {
        System.out.println("------------------------");
        System.out.println(ANSI_CODES.ANSI_YELLOW + msg + ANSI_CODES.ANSI_RESET);
        System.out.println("------------------------");
    }

    private void enterCmd(String msg) {
        System.out.print(ANSI_CODES.ANSI_CYAN + msg + ANSI_CODES.ANSI_RESET);
    }



    @SuppressWarnings("unused")
    static class ANSI_CODES {
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BLACK = "\u001B[30m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE = "\u001B[34m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_CYAN = "\u001B[36m";
        public static final String ANSI_WHITE = "\u001B[37m";
        public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
        public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
        public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
        public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
        public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
        public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
        public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
        public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    }

}
