package com.morpheusdata.backupTest

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.backup.AbstractBackupTypeProvider
import com.morpheusdata.core.backup.BackupExecutionProvider
import com.morpheusdata.core.backup.BackupRestoreProvider
import com.morpheusdata.core.backup.response.BackupExecutionResponse
import com.morpheusdata.core.backup.util.BackupStatusUtility
import com.morpheusdata.core.backup.response.BackupRestoreResponse
import com.morpheusdata.model.Backup
import com.morpheusdata.model.BackupProvider
import com.morpheusdata.model.BackupRestore
import com.morpheusdata.model.BackupResult
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.Workload
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Instance
import com.morpheusdata.model.OptionType
import com.morpheusdata.response.ServiceResponse
import groovy.util.logging.Slf4j

//
// Equivalent to morpheus BackupType
//
@Slf4j
class BackupTestTypeProvider extends AbstractBackupTypeProvider {

	BackupTestTypeProvider(Plugin plugin, MorpheusContext morpheusContext) {
		super(plugin, morpheusContext)
	}

	@Override
	BackupExecutionProvider getExecutionProvider() {
		return null
	}

	@Override
	BackupRestoreProvider getRestoreProvider() {
		return null
	}

	@Override
	String getCode() {
		return "backupTestTypeProvider"
	}

	@Override
	String getName() {
		return "Backup Test Type Provider"
	}

	@Override
	String getContainerType() {
		return "single"
	}

	@Override
	Boolean getCopyToStore() {
		return false
	}

	@Override
	Boolean getDownloadEnabled() {
		return false
	}

	@Override
	Boolean getRestoreExistingEnabled() {
		return true
	}

	@Override
	Boolean getRestoreNewEnabled() {
		return true
	}

	@Override
	String getRestoreType() {
		return "offline"
	}

	@Override
	String getRestoreNewMode() {
		return "VM_RESTORE"
	}

	@Override
	Boolean getHasCopyToStore() {
		return false
	}

	@Override
	Collection<OptionType> getOptionTypes() {
		return new ArrayList<OptionType>()
	}

	@Override
	ServiceResponse configureBackup(Backup backup, Map config, Map opts) {
		log.debug("configureBackup")
		log.debug("config: ${config}")
		log.debug("opts: ${opts}")
		backup.setConfigProperty("color", config?.config?.color)
		backup.setConfigProperty("foo", config?.config?.foo)
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse validateBackup(Backup backup, Map config, Map opts) {
		log.debug("validateBackup")
		BackupProvider backupProvider = backup.backupProvider
		log.debug("config: ${config}")
		log.debug("opts: ${opts}")
		log.debug("backupProvider.config: ${backupProvider.getConfig()}")

		return ServiceResponse.success()
	}

	@Override
	ServiceResponse createBackup(Backup backup, Map opts) {
		log.debug("createBackup")
		BackupProvider backupProvider = backup.backupProvider
		log.debug("backupProvider.name: ${backupProvider.name} ")
		log.debug("backupProvider.credentials, username: ${backupProvider.credentialData?.username}, pass: ${backupProvider.credentialData?.password} ")
		log.debug("backupProvider.username: ${backupProvider.username}")
		log.debug("backupProvider.pass: ${backupProvider.password}")
		log.debug("backupProvider.serviceToken: ${backupProvider.serviceToken}")
		log.debug("backupProvider.config.input1: ${backupProvider.getConfigProperty("input1")}")
		log.debug("backupProvider.config.input2: ${backupProvider.getConfigProperty("input2")}")
		log.debug("opts color: ${opts?.config?.color}")
		log.debug("opts foo: ${opts?.config?.foo}")

		return ServiceResponse.success()
	}

	@Override
	ServiceResponse deleteBackup(Backup backup, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse deleteBackupResult(BackupResult backupResult, Map opts) {
		ServiceResponse rtn = ServiceResponse.prepare()
		rtn.msg = "Unable to delete backup result"
		return rtn
	}

	@Override
	ServiceResponse prepareExecuteBackup(Backup backup, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse prepareBackupResult(BackupResult backupResult, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse<BackupExecutionResponse> executeBackup(Backup backup, BackupResult backupResult, Map executionConfig, Cloud cloud, ComputeServer computeServer, Map opts) {
		ServiceResponse<BackupExecutionResponse> rtn = ServiceResponse.prepare(new BackupExecutionResponse(backupResult))

		rtn.data.updates = true
		rtn.data.backupResult.status = BackupStatusUtility.SUCCEEDED
		rtn.success = true

		return rtn
	}

	@Override
	ServiceResponse<BackupExecutionResponse> refreshBackupResult(BackupResult backupResult) {
		ServiceResponse<BackupExecutionResponse> rtn = ServiceResponse.prepare(new BackupExecutionResponse(backupResult))

		rtn.data.updates = true
		rtn.data.backupResult.status = BackupStatusUtility.SUCCEEDED
		rtn.success = true

		return rtn
	}

	@Override
	ServiceResponse cancelBackup(BackupResult backupResult, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse extractBackup(BackupResult backupResult, Map opts) {
		return ServiceResponse.success()
	}

	// Backup Restore
	@Override
	ServiceResponse configureRestoreBackup(BackupResult backupResult, Map config, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse getBackupRestoreInstanceConfig(BackupResult backupResult, Instance instance, Map restoreConfig, Map opts) {
		log.debug("getBackupRestoreInstanceConfig - backupResult: $backupResult, instance: $instance, restoreConfig: $restoreConfig, opts: $opts")

		return ServiceResponse.success()
	}

	@Override
	ServiceResponse validateRestoreBackup(BackupResult backupResult, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse getRestoreOptions(Backup backup, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse<BackupRestoreResponse> restoreBackup(BackupRestore backupRestore, BackupResult backupResult, Backup backup, Map opts) {
		log.debug("restoreBackup - backupRestore: $backupRestore, backupResult: $backupResult, backup: $backup ")
		backupRestore.setConfigProperty("restoreResult1", "foo")
		backupRestore.setConfigProperty("restoreResult2", "bar")
		backupRestore.status = BackupStatusUtility.IN_PROGRESS
		
		log.debug("backupRestore restoreToNew: ${backupRestore.restoreToNew}")
		if(backupRestore.restoreToNew == true) {
			
			//
			// do some external restor operation
			// 
			
			log.debug("Restore to new instance")
			backupRestore.setConfigProperty("externalVmId", java.util.UUID.randomUUID())
		} else {
			// restore to existing
		}
		
		BackupRestoreResponse restoreResponse = new BackupRestoreResponse(backupRestore)
		restoreResponse.updates = true
		ServiceResponse rtn = ServiceResponse.prepare(restoreResponse)
		rtn.success = true
		
		return rtn
	}

	@Override
	ServiceResponse refreshBackupRestoreResult(BackupRestore backupRestore, BackupResult backupResult) {
		log.debug("refreshBackupRestoreResult - backupRestore: $backupRestore, backupResult: $backupResult ")
		def result1 = backupRestore.getConfigProperty("restoreResult1")
		def result2 = backupRestore.getConfigProperty("restoreResult2")
		log.debug("configProperties - result1: $result1, result2: $result2")
		
		String vmExternalId = backupRestore.getConfigProperty("externalVmId")

		log.debug("backupRestore restoreToNew: ${backupRestore.restoreToNew}")
		if(backupRestore.restoreToNew == true && vmExternalId) {
			log.debug("Restore to new instance")
			Workload workload = morpheus.workload.get(backupRestore.containerId).blockingGet()
			ComputeServer server = workload.server
			

			
			server.status = Workload.Status.running
			server.externalId = vmExternalId //vm.MOR.val
			server.internalId = vmExternalId //vm.config?.instanceUuid
			server.uniqueId = vmExternalId //vm.config?.uuid
			morpheus.computeServer.save([server]).blockingGet()
		} else {
			// restore to existing
		}
		
		if(restoreFailed) {
			Instance instance = workload.instance
			instance.status = Instance.Status.failed
			morpheus.instance.save([instance])
		}
	
		BackupRestoreResponse restoreResponse = new BackupRestoreResponse(backupRestore)
		
		def startTime = backupRestore.dateCreated.getTime()
		def currTime = new Date().getTime()
		def totalTime = currTime - startTime
		log.debug("elapsed backup time: $totalTime, totalTime > threshold: ${totalTime > 180000l}")
		if(totalTime > 180000l) {
			restoreResponse.backupRestore.status = BackupStatusUtility.SUCCEEDED
			restoreResponse.updates = true
		}
		
		log.debug("Backup restore status: ${restoreResponse.backupRestore.status}")
		ServiceResponse rtn = ServiceResponse.prepare(restoreResponse)
		rtn.success = true
		
		return rtn
	}
}
