import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class StopwatchServlet extends HttpServlet {
    private final ArrayList<Stopwatch> stopwatches = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] args;

        try {
            args = request.getPathInfo().split("/");

            if (args.length < 2 || args[1] == null) {
                throw new NullPointerException();
            }

            String result = stopwatches.get(Integer.parseInt(args[1])).get();
            response.setStatus(200);
            response.getWriter().print(result);

        } catch (NullPointerException | IOException e) {
            response.setStatus(400);
        } catch (NumberFormatException e) {
            response.setStatus(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] args;

        try {
            args = request.getPathInfo().split("/");
            if (args.length < 2 || !Objects.equals(args[1], "start")) {
                throw new Exception();
            }
            stopwatches.add(new Stopwatch());
            response.setStatus(201);
            response.getWriter().println(stopwatches.size() - 1);
        } catch (Exception e) {
            response.setStatus(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] args;

        try {
            args = request.getPathInfo().split("/");

            if (args.length < 3 || !Objects.equals(args[2], "lap")) {
                throw new IndexOutOfBoundsException();
            }

            response.setStatus(200);
            response.getWriter().print(stopwatches.get(Integer.parseInt(args[1])).lap());

        } catch (NullPointerException | IndexOutOfBoundsException e) {
            response.setStatus(400);
        } catch (NumberFormatException e) {
            response.setStatus(404);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] args;

        try {
            args = request.getPathInfo().split("/");

            if (args.length < 2 || args[1] == null) {
                throw new NullPointerException();
            }

            String result = stopwatches.get(Integer.parseInt(args[1])).stop();
            stopwatches.set(Integer.parseInt(args[1]), null);
            response.setStatus(200);
            response.getWriter().println(result);

        } catch (NullPointerException | IndexOutOfBoundsException e) {
            response.setStatus(400);
        } catch (NumberFormatException e) {
            response.setStatus(404);
        }
    }


}