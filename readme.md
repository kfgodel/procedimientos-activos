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

### Recompilar proyecto JS (no debería ser necesario en un checkout limpio)
> grunt  

### Levantar el server
Desde una IDE ejecutar la clase Java
ar.com.tenpines.html5poc.Html5PocMain.main()

### Recompilar JS mientras hacemos cambios
> grunt rebuild_on_changes  
(anda mal en windows porque el server Java lockea los archivos)

## Generar binarios
> mvn package  

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
