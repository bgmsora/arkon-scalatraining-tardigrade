Sobre este template trabajare como base (me lo recomendo Hornet)
https://github.com/tpolecat/doobie-http4s-sangria-graphql-example

docker-compose up -d
sbt
compile
run

cambie el nombre de sangria a graphql porque entraba en conflicto con
import sangria.schema.Schema


Voy a ocupar el link, para obtener la informacion
https://www.inegi.org.mx/app/api/denue/v1/consulta/Buscar/todos/lat,lng/100/ApiToken

Si ocupas 
sbt core/run 
solo funciona si lo tienes en la carpeta init


