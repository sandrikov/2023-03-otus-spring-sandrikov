# Бабочкарий

Задача: выращивать бабочек для выставки.  

## Служба снабжения
Служба снабжения [SupplierService](src/main/java/ru/otus/homework/butterflygarden/services/SupplierService.java)
через `supplierChannel` поставляет в бабочкарий партии [Shipment](src/main/java/ru/otus/homework/butterflygarden/domain/Shipment.java)
гусениц [Caterpillar](src/main/java/ru/otus/homework/butterflygarden/domain/Caterpillar.java) разного размера.   

## Выращивание бабочек

Логика обработки очередной партии
[ButterflyGardenConfig.butterflyGardenFlow()](src/main/java/ru/otus/homework/butterflygarden/config/ButterflyGardenConfig.java):
* партию распаковывают `transform(Shipment::batch)`
* разбирают поштучно `split()`
* обслуживают асинхронно `channel(c -> c.executor(Executors.newCachedThreadPool()))`
* в оранжерее [LifeCycleService](src/main/java/ru/otus/homework/butterflygarden/services/LifeCycleServiceImpl.java):
  * гусеницы с разной скоростью превращаются в коконы
   [Pupa](src/main/java/ru/otus/homework/butterflygarden/domain/Pupa.java) `LifeCycleService.growPupa()`
  * коконы превращаются в бабочек
   [Butterfly](src/main/java/ru/otus/homework/butterflygarden/domain/Butterfly.java) `LifeCycleService.growButterfly()`
* когда появились все бабочки `aggregate()`
* готовят отгрузку `transform(Delivery::new)`
* отправляют на выставку `exhibitionChannel()`

