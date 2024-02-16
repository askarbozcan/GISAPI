### GIS Project API
GIS API for U. Konstanz Proj
## Installation
1. Clone the repository
2. cd into the project directory
3. Run `docker build -t gis-api .` to build the docker image
4. Run `docker run -p 7123:7123 gis-api` to run the docker container

## Usage
The API is available at `http://localhost:7123/`
Two endpoints exist: 

---

POST `http://localhost:7123/polygons`

To use it, send a POST request with a body similar to this:
```json
{
"topLeft": {
"lat": 47.81625092610641,
"lng": 9.00424938326418
},
"bottomRight": {
"lat": 47.627785484985694,
"lng": 9.286460666364624
},
"zoomLevel": 1,
"amenities": [
"grocery",
"barber"
],
"nPolys": 10
}
```

You will receive a GeoJSON response similar to this:
```json
{
   "features":[
      {
         "geometry":{
            "coordinates":[
               [
                  [
                     9.139655182762802,
                     47.77169963855227
                  ],
                  [
                     9.139678747251203,
                     47.77180911322135
                  ],
                  [
                     9.13954738599433,
                     47.77182188526754
                  ],
                  [
                     9.13952382150593,
                     47.77171241062536
                  ],
                  [
                     9.139655182762802,
                     47.77169963855227
                  ]
               ]
            ],
            "type":"Polygon"
         },
         "properties":{
            "score":0.0,
            "height":4.44316291809082
         },
         "type":"Feature"
      },
      {
         "geometry":{
            "coordinates":[
               [
                  [
                     9.139921558177129,
                     47.6890788758129
                  ],
                  [
                     9.139999207029078,
                     47.68921178683312
                  ],
                  [
                     9.139839906954506,
                     47.68925396557947
                  ],
                  [
                     9.139762246135131,
                     47.689121057835436
                  ],
                  [
                     9.139921558177129,
                     47.6890788758129
                  ]
               ]
            ],
            "type":"Polygon"
         },
         "properties":{
            "score":0.0,
            "height":6.475737571716309
         },
         "type":"Feature"
      },
      {
         "geometry":{
            "coordinates":[
               [
                  [
                     9.141963933624012,
                     47.68464901474154
                  ],
                  [
                     9.141869471820595,
                     47.68467244807379
                  ],
                  [
                     9.14184892498624,
                     47.68463491026411
                  ],
                  [
                     9.141943388934664,
                     47.684611480833766
                  ],
                  [
                     9.141963933624012,
                     47.68464901474154
                  ]
               ]
            ],
            "type":"Polygon"
         },
         "properties":{
            "score":0.0,
            "height":2.0
         },
         "type":"Feature"

      }
   ],
   "type":"FeatureCollection"
}


```
---
GET `http://localhost:7123/amenities`: Lists all amenity types
