# Books. Docker version 


## Сборка проекта
```
mvn clean package
```


## Сборка образа вручную
```
docker build -t book-image:v1 .
docker run -p:8080:8080 --rm book-image:v1
```
## Служба снабжения
Служба снабжения [SupplierService](src/main/java/ru/otus/homework/butterflygarden/services/SupplierService.java)
через `supplierChannel` поставляет в бабочкарий партии [Shipment](src/main/java/ru/otus/homework/butterflygarden/domain/Shipment.java)
гусениц [Caterpillar](src/main/java/ru/otus/homework/butterflygarden/domain/Caterpillar.java) разного размера.   


