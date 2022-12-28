Sobre este template trabajare como base
https://github.com/tpolecat/doobie-http4s-sangria-graphql-example
No fue la mejor idea, casi todo esta deprecated y tuve que hacer muchisimas modificaciones

docker-compose up -d
sbt
compile
run

Voy a ocupar el link, para obtener la informacion
https://www.inegi.org.mx/app/api/denue/v1/consulta/Buscar/todos/lat,lng/100/ApiToken

Si ocupas 
sbt core/run 
solo funciona si lo tienes en la carpeta init


//Consultas

/////////Fetch all///////////
query{
  stratums{
    id
    name
  }
}
query{
  activities{
    id
    name
  }
}
query{
  shopTypes{
    id
    name
  }
}
query{
  shopById{
    id
    name
    activity{
      id
      name
    }
    address
    lat
    long
    shopType{
      id
      name
    }
    stratum{
      id
      name
    }
    website
  }
}


//////////Fetch By id////////
query{
  stratumsById(id:"1"){
    id
    name
  }
}
query{
  activitiesById(id:"112311"){
    id
    name
  }
}
query{
  shopTypesById(id:"1"){
    id
    name
  }
}
query{
  shopById(id:"34186"){
    id
    name
    activity{
      id
      name
    }
    address
    lat
    long
    shopType{
      id
      name
    }
    stratum{
      id
      name
    }
    website
  }
}