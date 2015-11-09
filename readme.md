# ateam-html5-poc

Prueba de concepto para crear una aplicación web con un framework JS para frontend y html5

## Setup desarrollo

### Frontend
Bajar como proyecto independiente [Ember-cli POC](https://github.com/kfgodel/ember-cli-poc)  
Y setupearlo segun sus instrucciones.  

Una vez que se obtiene el "compilado" en /dist usar `mvn install` o `mvn deploy` para generar una nueva version de maven.
La cual se puede usar desde el backend como dependencia.

### Backend
Para desarrollar sólo es necesario invocar
> mvn compile  

Que genera las clases "auto generadas" de querydsl, y descomprime el frontend dentro de los resources del classpath.  
Luego es  ejecutar la clase Java
ar.com.tenpines.html5poc.Html5PocMain.main()

## Generar binarios entregables
> mvn package  

Genera un zip que además incluye varios ejecutables (segun plataforma) para poder ejecutar la aplicación java como
si fuera nativa.

### Probar el zip final
> cd target  
> unzip ateam-html5-poc.zip  
 (descomprime en una carpeta con ejecutables)    
> cd ateam-html5-poc/bin  
> ./wrapper.sh console  

Abrir browser en [http://localhsot:9090](http://localhsot:9090) (debería verse la app)
Ctrl+C para salir


### deployar el zip
> mvn deploy  
