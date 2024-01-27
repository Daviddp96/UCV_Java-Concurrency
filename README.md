## [Proyecto Concurrencia]
# Programa de Procesamiento Concurrente

## Procesos
El sistema consta de los siguientes procesos:

**Main**: Es el proceso principal que coordina la ejecución del programa. Lee el número de SHAs y crea los hilos correspondientes. Además, lee las líneas del archivo de texto y las asigna a un monitor para su procesamiento.

**SHA**: Cada SHA es un proceso independiente que se ejecuta en un hilo. Recibe líneas del monitor, las procesa y muestra los resultados por pantalla.

## Recursos Críticos

**MonitorOK**: Es un monitor compartido entre los hilos SHA y el hilo Main. Los recursos críticos dentro del monitor son:
linea: Representa la línea actual que se asigna a un SHA para su procesamiento.
terminado: Indica si se ha alcanzado el final del archivo (eot-ok) y se deben detener los hilos SHA.

## Operaciones de Acceso
El monitor MonitorOK proporciona las siguientes operaciones de acceso a los recursos críticos:

**asignarLinea**: comprueba si la variable `linea` no es nula y si el estado `finished` es falso. Esto se hace en un bucle `while`, por lo que el hilo se quedará esperando mientras se cumpla esta condición. El propósito de esta verificación es evitar que se asigne una nueva línea si ya hay una línea pendiente de procesamiento y el programa aún no ha finalizado (`finished` es falso).

- Si la condición del bucle no se cumple, significa que el hilo puede asignar una nueva línea. Asigna la línea proporcionada (`linea`) a la variable `this.linea`, que representa la línea actual en el monitor.

- Después de asignar la línea, se llama al método `notifyAll()`. Esto notifica a todos los hilos que están esperando en el monitor que la línea ha sido establecida. Los hilos en espera pueden despertar y continuar su ejecución.

**procesarLinea**: toma una línea del monitor y la procesa. El hilo se quedará esperando hasta que haya una línea disponible o hasta que el programa haya finalizado. Una vez obtenida la línea, se marca como procesada y se notifica a los demás hilos.

**setTerminado**: Indica al monitor que se ha alcanzado el final del archivo y se deben detener los hilos SHA.

## Condiciones de Sincronización

La condición de sincronización `this.linea != null && !terminado` se evalúa cuando un hilo intenta asignar una línea al recurso crítico linea del monitor.

La condición se descompone de la siguiente manera:

**this.linea != null**: Verifica si la línea actual (this.linea) es diferente de null. Esto significa que ya hay una línea asignada al recurso linea, lo que indica que está ocupado y se debe esperar a que sea procesada por un SHA antes de asignar una nueva línea.

**!terminado**: Verifica si la variable terminado del monitor es false. Si terminado es true, significa que se ha alcanzado el final del archivo y no se deben asignar más líneas. En ese caso, el hilo debe detenerse y no asignar ninguna línea adicional.

La condición completa garantiza que el hilo que intenta asignar una línea espere si el recurso linea ya está ocupado o si se ha alcanzado el final del archivo. De esta manera, se evita la asignación de líneas innecesarias o la sobreescritura de líneas que aún no han sido procesadas por los hilos SHA.

El monitor utiliza `notifyAll` para notificar a los hilos SHA cuando una línea está disponible o cuando se alcanza el final del archivo.

## Decisiones de Diseño

**Uso de hilos**: Se utiliza un enfoque de hilos para permitir que múltiples SHAs procesen las líneas simultáneamente, mejorando así la eficiencia del sistema.

**Uso de un monitor**: Se utiliza el monitor MonitorOK para coordinar el acceso y la asignación de líneas entre el hilo Main y los hilos SHA, garantizando la sincronización adecuada y evita condiciones de carrera.

**Manejo de errores**: El sistema maneja situaciones de error, como el número incorrecto de argumentos o un número inválido de SHAs. Muestra mensajes de error correspondientes y finaliza la ejecución si se produce un error.

**Espera aleatoria**: Cada SHA espera un tiempo aleatorio, que sigue la descripción del enunciado, después de procesar una línea, simulando un procesamiento realista. Esto ayuda a demostrar la capacidad de procesamiento concurrente del sistema.

## Referencias

Para poder leer el archivo de texto: https://www.javatpoint.com/java-filereader-class


Para los try catch:  https://www.dit.upm.es/~pepe/libros/concurrency/index.html#!1030
                     https://codegym.cc/es/groups/posts/es.588.numberformatexception-en-java
                     https://www.w3api.com/Java/IOException/

Para convertir de texto a entero: https://www.javatpoint.com/java-integer-parseint-method
