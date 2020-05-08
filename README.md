# Games (Java - Maven)
Tic Tac Toe, El lobo y las obejas y Ajedrez

Para iniciar los juegos hay que importar el proyecto como maven.
Una vez importado hacemos click en "Run Configuration" y en parametros se leccionamos el juego , modo y tipo de jugador:
  
   - game: TTT (para tic tac toe) WAS (para lobo y obejas) ó CHESS (para el ajedrez)
   - mode: gui (para usar Swing) ó console (para usar en modo consola)
   - Player: manual (para un jugador manual), random (para uno aleatorio) ó smart (para uno inteligente).
    
   * Ej: ttt gui manual smart (Para jugar al tic tac toe usando Swing, un jugador manual y otro inteligente. 

Estos juegos desarrolados con Maven y Java usan hilos concurrencia para mejorar el funcionamiento del jugador inteligente.

Algoritmos de inteligencia utilizados MinMax y  ConcurrentDeepeningMinMax (CDMM) que permite lanzarlo de forma concurrente.
En su constructor se pueden especificar cuántos hilos concurrentes lanzará como máximo
Ten en cuenta que, una vez que cada núcleo del procesador está ejecutando un hilo, ya no será posible mejorar el rendimiento intentando lanzar más hilos. Puedes saber (desde Java) cuántos hilos puedes usar como máximo llamando a Runtime.getRuntime ().availableProcessors().

![GitHub Logo](/src/chess.png)

![GitHub Logo](/images/was.png)

![GitHub Logo](/images/ttt.png)
