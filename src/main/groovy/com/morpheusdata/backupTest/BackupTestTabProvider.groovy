package com.morpheusdata.backupTest

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.providers.AbstractBackupIntegrationTabProvider
import com.morpheusdata.model.Account
import com.morpheusdata.model.BackupProvider
import com.morpheusdata.model.User
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel

class BackupTestTabProvider extends AbstractBackupIntegrationTabProvider {

	Plugin plugin;
	MorpheusContext morpheusContext;

	BackupTestTabProvider(Plugin plugin, MorpheusContext morpheus) {
		this.plugin = plugin
		this.morpheusContext = morpheus
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
		return "backup-test-tab-provider"
	}

	@Override
	String getName() {
		return "Backup Tab Test Provider"
	}

	@Override
	HTMLResponse renderTemplate(BackupProvider backupProvider) {
		ViewModel<BackupProvider> model = new ViewModel<>()
		model.object = backupProvider

		return getRenderer().renderTemplate("hbs/backupTestProviderTab", model);
	}

	@Override
	Boolean show(BackupProvider backupProvider, User user, Account account) {
		return backupProvider.type.code == BackupTestProvider.PROVIDER_CODE
	}


}
