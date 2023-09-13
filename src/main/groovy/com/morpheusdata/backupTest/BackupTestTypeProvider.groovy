package com.morpheusdata.backupTest

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.backup.AbstractBackupTypeProvider
import com.morpheusdata.core.backup.BackupExecutionProvider
import com.morpheusdata.core.backup.BackupRestoreProvider
import com.morpheusdata.model.Backup
import com.morpheusdata.model.BackupProvider
import com.morpheusdata.model.BackupRestore
import com.morpheusdata.model.BackupResult
import com.morpheusdata.model.Cloud
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
		return "VM"
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
		backup.setConfigProperty("color", config.config.color)
		backup.setConfigProperty("foo", config.config.foo)
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
		log.debug("backupProvider.credentials, username: ${backupProvider.credentialData?.username}, pass: ${backupProvider.credentialData?.password} ")
		log.debug("backupProvider.username: ${backupProvider.username}")
		log.debug("backupProvider.pass: ${backupProvider.password}")
		log.debug("backupProvider.serviceToken: ${backupProvider.serviceToken}")
		log.debug("backupProvider.config.input1: ${backupProvider.getConfigProperty("input1")}")
		log.debug("backupProvider.config.input2: ${backupProvider.getConfigProperty("input2")}")

		return ServiceResponse.success()
	}

	@Override
	ServiceResponse deleteBackup(Backup backup, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse deleteBackupResult(BackupResult backupResultModel, Map opts) {
		ServiceResponse rtn = ServiceResponse.prepare()
		rtn.msg = "Unable to delete backup result"
		return rtn
	}

	@Override
	ServiceResponse prepareExecuteBackup(Backup backup, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse prepareBackupResult(BackupResult backupResultModel, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse executeBackup(Backup backup, BackupResult backupResult, Map executionConfig, Cloud cloud, ComputeServer computeServer, Map opts) {
		return ServiceResponse.success()
	}

	@Override
		ServiceResponse refreshBackupResult(BackupResult backupResult) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse cancelBackup(BackupResult backupResultModel, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse extractBackup(BackupResult backupResultModel, Map opts) {
		return ServiceResponse.success()
	}

	// Backup Restore
	@Override
	ServiceResponse configureRestoreBackup(BackupResult backupResultModel, Map config, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse getBackupRestoreInstanceConfig(BackupResult backupResultModel, Instance instanceModel, Map restoreConfig, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse validateRestoreBackup(BackupResult backupResultModel, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse getRestoreOptions(Backup backup, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse restoreBackup(BackupRestore backupRestore, BackupResult backupResultModel, Backup backup, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse refreshBackupRestoreResult(BackupRestore backupRestoreModel, BackupResult backupResultModel) {
		return ServiceResponse.success()
	}
}
