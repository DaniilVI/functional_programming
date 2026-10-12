package ru.yarsu.web.routes

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.PATCH
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer
import ru.yarsu.Filter.errorFilter
import ru.yarsu.Filter.jsonContentTypeFilter
import ru.yarsu.Filter.loggingFilter
import ru.yarsu.data.LaunderetteStorage
import ru.yarsu.data.MachineStorage
import ru.yarsu.data.UserStorage
import ru.yarsu.operations.CheckLaunderetteUUIDOperationImpl
import ru.yarsu.operations.CheckMachineUUIDOperationImpl
import ru.yarsu.operations.CheckUserUUIDOperationImpl
import ru.yarsu.operations.CreateMachineOperationImpl
import ru.yarsu.operations.CreateUserOperationImpl
import ru.yarsu.operations.DeleteLaunderetteOperationImpl
import ru.yarsu.operations.EditMachineOperationImpl
import ru.yarsu.operations.FilterMachineByTimeOperationImpl
import ru.yarsu.operations.FilterMachineByTypeOperationImpl
import ru.yarsu.operations.FilterMachinesByWeightAndBudgetOperationImpl
import ru.yarsu.operations.GetAllMachinesOperationImpl
import ru.yarsu.operations.GetAllMachinesPagingOperationImpl
import ru.yarsu.operations.GetAllUsersOperationImpl
import ru.yarsu.operations.GetIncorrectValueOperationImpl
import ru.yarsu.operations.GetJsonEditMachineFieldsOperationImpl
import ru.yarsu.operations.GetJsonMachineFieldsOperationImpl
import ru.yarsu.operations.GetLaunderetteFieldOperationImpl
import ru.yarsu.operations.GetMachineOperationImpl
import ru.yarsu.operations.GetUserFieldOperationImpl
import ru.yarsu.operations.GetUserFromEmailOperationImpl
import ru.yarsu.operations.UpdateUserOperationImpl
import ru.yarsu.web.routes.v2.AddNewMachineHandler
import ru.yarsu.web.routes.v2.AddNewUserHandler
import ru.yarsu.web.routes.v2.ChangeUserHandler
import ru.yarsu.web.routes.v2.DeleteLaunderetteHandler
import ru.yarsu.web.routes.v2.EditMachineHandler
import ru.yarsu.web.routes.v2.GetAllMachinesHandler
import ru.yarsu.web.routes.v2.GetAllUsersHandler
import ru.yarsu.web.routes.v2.GetMachineHandler
import ru.yarsu.web.routes.v2.GetMachinesSortingHandler
import ru.yarsu.web.routes.v2.StatisticByLifeHandler
import ru.yarsu.web.routes.v2.StatisticByTypeHandler

class ApplicationRoutes {
    private fun applicationRoutes(
        storageMachine: MachineStorage,
        storageLaunderette: LaunderetteStorage,
        storageUser: UserStorage,
    ): RoutingHttpHandler {
        val jsonFactory = JsonNodeFactory.instance

        val v2GET =
            routes(
                "users" bind GET to
                    GetAllUsersHandler(
                        jsonFactory,
                        GetAllUsersOperationImpl(storageUser),
                    ),
                "launderettes/{launderette-id}/machines/check-wash" bind GET to
                    GetMachinesSortingHandler(
                        jsonFactory,
                        CheckLaunderetteUUIDOperationImpl(storageLaunderette),
                        FilterMachinesByWeightAndBudgetOperationImpl(storageMachine),
                    ),
                "launderettes//machines/check-wash" bind GET to
                    GetMachinesSortingHandler(
                        jsonFactory,
                        CheckLaunderetteUUIDOperationImpl(storageLaunderette),
                        FilterMachinesByWeightAndBudgetOperationImpl(storageMachine),
                    ),
                "machines" bind GET to
                    GetAllMachinesHandler(
                        jsonFactory,
                        GetAllMachinesPagingOperationImpl(storageMachine),
                    ),
                "machines/{machine-id}" bind GET to
                    GetMachineHandler(
                        jsonFactory,
                        GetMachineOperationImpl(storageMachine),
                        GetUserFieldOperationImpl(storageUser),
                        GetLaunderetteFieldOperationImpl(storageLaunderette),
                    ),
                "machines/statistics/by-type" bind GET to
                    StatisticByTypeHandler(
                        jsonFactory,
                        FilterMachineByTypeOperationImpl(storageMachine),
                    ),
                "machines/statistics/by-life" bind GET to
                    StatisticByLifeHandler(
                        jsonFactory,
                        FilterMachineByTimeOperationImpl(storageMachine),
                    ),
            )
        val v2POST =
            routes(
                "users" bind POST to
                    AddNewUserHandler(
                        jsonFactory,
                        CreateUserOperationImpl(storageUser),
                        GetUserFromEmailOperationImpl(storageUser),
                        GetIncorrectValueOperationImpl(),
                    ),
                "users/{user-id-from}/machines-to/{user-id-to}" bind POST to
                    ChangeUserHandler(
                        jsonFactory,
                        CheckUserUUIDOperationImpl(storageUser),
                        UpdateUserOperationImpl(storageMachine),
                    ),
                "users//machines-to/{user-id-to}" bind POST to
                    ChangeUserHandler(
                        jsonFactory,
                        CheckUserUUIDOperationImpl(storageUser),
                        UpdateUserOperationImpl(storageMachine),
                    ),
                "users//machines-to/" bind POST to
                    ChangeUserHandler(
                        jsonFactory,
                        CheckUserUUIDOperationImpl(storageUser),
                        UpdateUserOperationImpl(storageMachine),
                    ),
                "users/{user-id-from}/machines-to/" bind POST to
                    ChangeUserHandler(
                        jsonFactory,
                        CheckUserUUIDOperationImpl(storageUser),
                        UpdateUserOperationImpl(storageMachine),
                    ),
                "machines" bind POST to
                    AddNewMachineHandler(
                        jsonFactory,
                        GetJsonMachineFieldsOperationImpl(storageLaunderette, storageUser),
                        CreateMachineOperationImpl(storageMachine),
                    ),
            )
        val v2DELETE =
            routes(
                "launderettes/{launderette-id}" bind DELETE to
                    DeleteLaunderetteHandler(
                        jsonFactory,
                        DeleteLaunderetteOperationImpl(storageLaunderette, storageMachine),
                        GetAllMachinesOperationImpl(storageMachine),
                    ),
                "launderettes/" bind DELETE to
                    DeleteLaunderetteHandler(
                        jsonFactory,
                        DeleteLaunderetteOperationImpl(storageLaunderette, storageMachine),
                        GetAllMachinesOperationImpl(storageMachine),
                    ),
            )
        val v2PATCH =
            routes(
                "machines/{machine-id}" bind PATCH to
                    EditMachineHandler(
                        jsonFactory,
                        GetJsonEditMachineFieldsOperationImpl(storageLaunderette, storageUser),
                        CheckMachineUUIDOperationImpl(storageMachine),
                        EditMachineOperationImpl(storageMachine),
                    ),
            )
        val appHandler =
            routes(
                "/" bind GET to Index(),
                "ping" bind GET to { Response(OK) },
                "v2" bind GET to v2GET,
                "v2" bind POST to v2POST,
                "v2" bind DELETE to v2DELETE,
                "v2" bind PATCH to v2PATCH,
            )
        val app = jsonContentTypeFilter().then(errorFilter()).then(loggingFilter()).then(appHandler)
        return app
    }

    // A
    fun startServer(
        port: Int,
        storageMachine: MachineStorage,
        storageLaunderette: LaunderetteStorage,
        storageUser: UserStorage,
    ): Int {
        if (port in 1024..65536) {
            val app =
                applicationRoutes(
                    storageMachine,
                    storageLaunderette,
                    storageUser,
                )
            app.asServer(Netty(port)).start()
        } else {
            return 1
        }
        return 0
    }
}
