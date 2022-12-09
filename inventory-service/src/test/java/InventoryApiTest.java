import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class InventoryApiTest {

  @Test
  public void testAddProductConcurrently() throws IOException {
    String command = "curl -X POST https://postman-echo.com/post --data foo1=bar1&foo2=bar2";
    Process process = Runtime.getRuntime().exec(command);
    List<Callable<ResponseEntity>> curls = new ArrayList<>();
    for (int i = 0; i < 11; i++) {
      curls.add(new Callable<ResponseEntity>() {
        @Override
        public ResponseEntity call() throws Exception {
          String command = "curl -X POST https://postman-echo.com/post --data foo1=bar1&foo2=bar2";
          Process process = Runtime.getRuntime().exec(command);
          return null;
        }
      });
    }

    ExecutorService executor = Executors.newFixedThreadPool(11);

    PriorityQueue<ResponseEntity> raceResults = new PriorityQueue<>();
    List<Future<ResponseEntity>> futures;
//    try {
//      futures = executor.invokeAll(robots);
//    } catch (InterruptedException e) {
//      throw new RuntimeException(e);
//    }
    executor.shutdown();
  }

  @Test
  public void test1() throws IOException {
  }

  @Test
  public void test() throws IOException {
    String[] command = new String[]{"curl", "--location", "--request", "POST",
        "'http://localhost:8081/inventory/1/products/'", "--header", "'username: admin'",
        "--header", "'password: admin'", "--header", "'Content-Type: application/json'",
        "--data-raw", "'{\"productId\": 1,\"quantity\": 900}'"};
//    Process process = Runtime.getRuntime().exec(command);
//    System.out.println(process);
//    process.destroy();
//    String command = "curl --location --request POST 'http://localhost:8081/inventory/1/products/' \\\n"
//        + "--header 'username: admin' \\\n"
//        + "--header 'password: admin' \\\n"
//        + "--header 'Content-Type: application/json' \\\n"
//        + "--data-raw '{\n"
//        + "    \"productId\": 1,\n"
//        + "    \"quantity\": 900\n"
//        + "}'";
    try {
      ProcessBuilder pb = new ProcessBuilder(command);
// errorstream of the process will be redirected to standard output
      pb.redirectErrorStream(true);
// start the process
      Process proc = pb.start();
//      Process proc = Runtime.getRuntime().exec(command);
      /* get the inputstream from the process which would get printed on
       * the console / terminal
       */
      InputStream ins = proc.getInputStream();
// creating a buffered reader
      BufferedReader read = new BufferedReader(new InputStreamReader(ins));
      StringBuilder sb = new StringBuilder();
      read
          .lines()
          .forEach(line -> {
            System.out.println("line>" + line);
            sb.append(line);
          });
// close the buffered reader
      read.close();
      /*
       * wait until process completes, this should be always after the
       * input_stream of processbuilder is read to avoid deadlock
       * situations
       */
      proc.waitFor();
      /* exit code can be obtained only after process completes, 0
       * indicates a successful completion
       */
      int exitCode = proc.exitValue();
      System.out.println("exit code::" + exitCode);
// finally destroy the process
      proc.destroy();
    } catch (UnsupportedOperationException | IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
