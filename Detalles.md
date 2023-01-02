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

//NearbyShops 
query{
  nearbyShops(latitude:19.4324, longitude:-80.8663, limit:3){
    name
  }
}

//shopsInRadius
query{
  shopsInRadius(latitude:-99.196237, longitude:19.49, radius:50000){
    name
  }
}

//////////Mutation////////
mutation CreateShop(
  $shopInput: ShopInput!) {
  createShop(input: $shopInput) 
}
{
  "shopInput": {
    "id": 3418,
    "activityId": 1,
    "shopTypeId": 1,
    "stratumId": 1,
    "name": "Asereje",
    "businessName": "123",
    "address": "Azcapotzalco",
    "phoneNumber": "123123",
    "website": "asdas.com",
    "lat": 19.491610160571422,
    "long": -99.19624704525096,
    "email": "asdasda@gmail.com"
  }
}