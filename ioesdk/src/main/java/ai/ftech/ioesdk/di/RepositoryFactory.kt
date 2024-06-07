package ai.ftech.ioesdk.di

import ai.ftech.ioesdk.data.repo.IOERepoImpl
import ai.ftech.ioesdk.domain.repo.IIOERepo

object RepositoryFactory {
    private val ioeRepo = IOERepoImpl()

    fun getIOE(): IIOERepo {
        return ioeRepo
    }
}
