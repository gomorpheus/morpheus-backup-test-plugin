package com.morpheusdata.backupTest

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.OptionSourceProvider
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.projection.ReferenceDataSyncProjection
import groovy.util.logging.Slf4j

@Slf4j
class BackupTestOptionSourceProvider implements OptionSourceProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	BackupTestOptionSourceProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getCode() {
		return 'backup-test-option-source'
	}

	@Override
	String getName() {
		return 'Backup Test Option Source'
	}

	@Override
	List<String> getMethodNames() {
		return new ArrayList<String>(['colorOptions'])
	}

	def colorOptions(args) {
		log.debug("Backup Test Color Options")
		return [
			[name: "Red", value: "red"],
			[name: "Green", value: "green"],
			[name: "Blue", value: "blue"]
		]
	}
}
