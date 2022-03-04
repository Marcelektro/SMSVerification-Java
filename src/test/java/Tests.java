public class Tests {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("You must provide your API Key as the first argument!");
            System.exit(-1);
            return;
        }

        new TestSession( args[0] ); // Pass the api key!
    }

}
