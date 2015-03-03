# ateam-html5-poc

Prueba de concepto para crear una aplicación web con un framework JS para frontend y html5


## Setup desarrollo

### Instalar herramientas

> sudo apt-get install nodejs  
> sudo npm install -g bower  
> sudo npm install -g grunt-cli  

### Bajar dependencias js
> npm install  
> bower install  

### Setupear la parte JS del proyecto para empezar desarrollo
> grunt setup_project  

### Levantar el server
ar.com.tenpines.html5poc.Html5PocMain.main()

## Generar binarios

### Probar el zip final
> mvn package  
> cd target  
> unzip ateam-html5-poc.zip  
 (descomprime en una carpeta con ejecutables)  
> cd ateam-html5-poc/bin  
> ./wrapper.sh console  

Abrir browser en http://localhsot:9090 (debería verse la app)
Ctrl+C para salir


### deployar el zip
> mvn deploy  
