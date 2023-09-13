package com.morpheusdata.backupTest

import com.morpheusdata.core.Plugin
import groovy.util.logging.Slf4j

@Slf4j
class BackupTestPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-backup-test-plugin'
	}

	@Override
	void initialize() {
		this.name = "Backup Test Plugin"
		BackupTestProvider backupProvider = new BackupTestProvider(this, morpheus)
		registerProvider(backupProvider)

		BackupTestOptionSourceProvider optionSourceProvider = new BackupTestOptionSourceProvider(this, morpheus)
		registerProvider(optionSourceProvider)
	}

	@Override
	void onDestroy() {
		// nothing
	}
}
