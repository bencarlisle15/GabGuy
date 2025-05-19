import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cli {

    private static final long serialVersionUID = 1L;
    private final Magpie mag;
    private final BufferedReader stdinReader;

    public Cli() {
        this.mag = new Magpie();
        this.stdinReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void startPrintingResponses() {
        String response;
        while (true) {
            response = this.mag.getResponse();
            if (response != null) {
                System.out.println(response);
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void execute() throws IOException {
        String line;
        System.out.println("Hi I'm GabGuy, let's talk\nType `help` for help.");

        while ((line = this.stdinReader.readLine()) != null) {
            this.mag.setStatement(line);
            this.mag.run();
        }
        System.out.println("Exiting...");
    }

    public static void main(String[] args) {
        Cli driver = new Cli();
        try {
            new Thread(driver::startPrintingResponses).start();
           driver.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}