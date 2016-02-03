# proj4m
proj4 minimal/maptalks

## supported projection
- Normal
    - EPSG:3857, `+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext  +no_defs`
    - EPSG:4326, `+proj=longlat +datum=WGS84 +no_defs`
- GCJ02
    - GCJ02, `+proj=longlat +dataum=GCJ02`
    - GCJ02MC, `+proj=merc +datum=GCJ02`
- BD09
    - BD09LL, `+proj=longlat +datum=BD09`
    - BD09MC, `+proj=bmerc +datum=BD09`

## Usage
```
Proj4 proj = new Proj4("GCJ02", "+proj=merc +datum=GCJ02");
proj.forward(new double[]{120.0, 30.0});
```
