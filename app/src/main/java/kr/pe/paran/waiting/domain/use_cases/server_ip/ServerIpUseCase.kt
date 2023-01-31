package kr.pe.paran.waiting.domain.use_cases.server_ip

import kr.pe.paran.waiting.data.repository.Repository

class ServerIpUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(ipAddress: String) {
        repository.saveSeverIp(ipAddress = ipAddress)
    }

    suspend operator fun invoke(): String {
        return repository.loadServerIp()
    }
}
