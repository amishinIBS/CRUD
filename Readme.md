### Библиотека для CRUD операций над сущностями в spring-data-jpa через REST

#
#### Структура

* Автоконфигурация для поднятия дефолтных контроллеров для стартера spring-boot.

      ru.ibs.crud.spring.config.DefaultCrudConfig

* Дефолтные контроллеры для CRUD операций и поиска.

        DefaultCrudController
        DefaultFindController

* DTO для результатов поиска

        ru.ibs.crud.spring.dto.ResponseDto

* Интерфейсы и классы для реализации ФЛК

      ru.ibs.crud.spring.flk.ValidationCondition - интерфейс условия проверки с кодом ошибки и её сообщением
      ru.ibs.crud.spring.flk.ValidationResult - результат проверки ValidationCondition
      ru.ibs.crud.spring.flk.ValidationContext - контекст валидации передаваемый в метод CRUDService при совершении операций CRUD
      ru.ibs.crud.spring.flk.ValidationService - интерфейс сервиса валиадции

* Интерфейс маппера для отображения Entity в Dto и его дефолтная абстрактная реализация

      ru.ibs.crud.spring.mapper.Mapper
      ru.ibs.crud.spring.mapper.AbstractMapperImpl

* Классы и интерфейсы для работы с метаданными сущностей, над которыми осуществляются операции CRUD

      ru.ibs.crud.spring.metadata.EntityMetadata - Класс для описания метаданных сущности, над которой делаем CRUD 
      ru.ibs.crud.spring.metadata.EntityMetadataProvider - Интерфейс провайдера метаданных
      ru.ibs.crud.spring.metadata.DefaultEntityMetadataProviderImpl - Дефолтная реализация провайдера метаданных

* Классы и интерфейсы сервисов операций поиска и CRUD

      ru.ibs.crud.spring.services.interceptors - Интерфейсы и дефолтные реализации интерцепторов для операций create и update
      ru.ibs.crud.spring.services.CRUDService - Интерфейс сервиса CRUD операций
      AbstractCrudServiceImpl - Дефолтная абстрактная реализация сервиса CRUD операций
      ru.ibs.crud.spring.services.FindService - Интерфейс сервиса поиска сущностей
      ru.ibs.crud.spring.services.AbstractFindService - Дефолтная абстрактная реализация сервиса поиска сущностей

#
#### Как использовать
1. В приложении реализуем классы DTO,ENTITY,JpaRepository<ENTITY,TYPE_ID> для требуемых сущностей.

2. Объявляем бины, реализующие:
    * (если необходимо делать что-то перед операциями сохранения) InterceptorPreCreate<ENTITY, DTO>, InterceptorPreUpdate<ENTITY, DTO>
    * CRUDService<TYPE_ID, DTO, CONDITION extends ValidationCondition<CODE, MESSAGE>>
    * Mapper<MODEL, DTO>
    * ValidationService<ENTITY, CONDITION extends ValidationCondition<CODE, MESSAGE>>
    * FindService<<DTO>>

3. Контроллеры вызывающие соответствующие методы сервисов. Для spring-boot можно подключить стартер, для остальных случаев можно унаследоваться от


    DefaultCrudController
    DefaultFindController 