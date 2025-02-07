package co.edu.eci;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EciBoot {
    public static Map<String, Method> services = new HashMap<>();
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        loadComponents(args);
        System.out.println(simulateRequest("/greeting"));
        System.out.println(simulateRequest("/pi"));

    }
    private static String simulateRequest(String route) throws InvocationTargetException, IllegalAccessException {
        Method s = services.get(route); // buscar el método
        //String response = (String) s.invoke(null, "");
        String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n"
                + "\r\n"
                + "{\"PI\":"+ (String) s.invoke(null, "Pedro") +"}";
        return response;
    }

    public static void loadComponents(String []args){
        try{
            Class c = Class.forName(args[0]);
            if(!c.isAnnotationPresent(RestController.class)) {// preguntarles si no está con restcontroller
                System.exit(0); // toca cargarla del disco y esto es para cuando no está presente
            }
            for (Method m : c.getDeclaredMethods()){
                if(m.isAnnotationPresent(GetMapping.class)){ // ¿la anotación está presente?
                    GetMapping a = m.getAnnotation(GetMapping.class);
                    services.put(a.value(),m); // necesito sacar la anotation
                }
            }


        }catch ( Exception e
        ) {

        }

    }
}
