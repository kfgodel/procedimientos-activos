# Procedimientos Activos

Proyecto java web para administrar el conocimiento de procedural de un grupo.  


## Setup desarrollo

### Frontend
Bajar como proyecto independiente [Ember-cli POC](https://github.com/kfgodel/procedimientos-activos)  
Y setupearlo segun sus instrucciones.  

Una vez que se obtiene el "compilado" en /dist usar `mvn install` o `mvn deploy` para generar una nueva version del 
frontend disponible como dependencia maven. La cual se puede usar desde el backend como cualquier otra dependencia.

### Backend
Para desarrollar sólo es necesario invocar
> mvn compile  

Que genera las clases "auto generadas" de querydsl, y descomprime el frontend dentro de los resources del classpath.  
Luego es  ejecutar la clase Java
ar.com.kfgodel.proact.Html5PocMain.main()

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


### deployar el zip en su repo
> mvn deploy  

### Deployar en heroku
Repo git de heroku:
> git remote add heroku https://git.heroku.com/procedimientos-activos.git

> git push heroku master

Deployar vetemecum
> git push heroku_vetemecum DLG-2335_vetemecum:master
