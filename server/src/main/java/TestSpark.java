import spark.Spark;

public class TestSpark {

    public static void main(String[] args) {

        try{

            int port = Integer.parseInt(args[0]);

            Spark.port(port);

            Spark.get("/hi", (req, res) -> {

                return "Hello this is another program working!!!";

            });

            Spark.put("/test2/:param1", (req,res) -> {

                res.body(req.params("param1"));
                return "Success!";

            });

            Spark.awaitInitialization();
            System.out.println("I am on port " + port);
            Spark.notFound("incorrect path");

        }catch(Exception e){

            System.err.println("this isn't working...");

        }

    }
}
