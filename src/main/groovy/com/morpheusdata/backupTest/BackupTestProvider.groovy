package com.morpheusdata.backupTest

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.backup.AbstractBackupProvider
import com.morpheusdata.core.backup.BackupJobProvider
import com.morpheusdata.core.backup.DefaultBackupJobProvider
import com.morpheusdata.core.backup.BackupProvider
import com.morpheusdata.core.backup.BackupTypeProvider
import com.morpheusdata.model.BackupProvider as BackupProviderModel
import com.morpheusdata.model.BackupProviderType
import com.morpheusdata.model.Icon
import com.morpheusdata.model.OptionType
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.HandlebarsRenderer
import com.morpheusdata.views.Renderer
import com.morpheusdata.views.ViewModel
import groovy.util.logging.Slf4j
import groovy.json.JsonOutput
import com.morpheusdata.core.util.ConnectionUtils

//
// Equivalent to Morpheus BackupProviderType
//

@Slf4j
class BackupTestProvider extends AbstractBackupProvider {

	private HandlebarsRenderer renderer

	public static final String PROVIDER_CODE = 'backup-test'

	BackupTestProvider(Plugin plugin, MorpheusContext morpheusContext) {
		super(plugin, morpheusContext)

		BackupTestTypeProvider backupTypeProvider = new BackupTestTypeProvider(plugin, morpheus)
		plugin.registerProvider(backupTypeProvider)
		addScopedProvider(backupTypeProvider, "aws", null)
		addScopedProvider(backupTypeProvider, "manual", null)
		addScopedProvider(backupTypeProvider, "openstack", null)
		addScopedProvider(backupTypeProvider, "vmware", null)

		plugin.registerProvider(new BackupTestTabProvider(plugin, morpheus))
	}

	@Override
	String getCode() {
		return PROVIDER_CODE
	}

	@Override
	String getName() {
		return 'Backup Test'
	}

	@Override
	Icon getIcon() {
		return new Icon(path:"krusty.svg", darkPath: "krusty.svg")
	}

	@Override
	public Boolean getEnabled() { return true; }

	@Override
	public Boolean getCreatable() { return true; }

	@Override
	public Boolean getRestoreNewEnabled() { return true; }

	@Override
	public Boolean getHasBackups() { return true; }

	@Override
	public Boolean getHasCreateJob() { return true; }

	@Override
	public Boolean getHasCloneJob() { return false; }

	@Override
	public Boolean getHasAddToJob() { return false; }

	@Override
	public Boolean getHasOptionalJob() { return false; }

	@Override
	public Boolean getHasSchedule() { return false; }

	@Override
	public Boolean getHasJobs() { return false; }

	@Override
	public String getDefaultJobType() { return "create"; }

	@Override
	public Boolean getHasRetentionCount() { return false; }

	@Override
	Collection<OptionType> getOptionTypes() {
		Collection<OptionType> optionTypes = new ArrayList();
		optionTypes << new OptionType(
			code:"backupProviderType.backupTest.host", inputType:OptionType.InputType.TEXT, name:'host', category:"backupProviderType.backupTest",
			fieldName:'host', fieldCode: 'gomorpheus.optiontype.Host', fieldLabel:'Host', fieldContext:'domain', fieldGroup:'default',
			required:true, enabled:true, editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:10, fieldClass:null
		)
		optionTypes << new OptionType(
			code:"backupProviderType.backupTest.credential", inputType:OptionType.InputType.CREDENTIAL, name:'credentials', category:"backupProviderType.backupTest",
			fieldName:'type', fieldCode:'gomorpheus.label.credentials', fieldLabel:'Credentials', fieldContext:'credential', optionSource:'credentials',
			required:true, enabled:true, editable:true, global:false, placeHolder:null, helpBlock:'Choose the desired credential type', defaultValue:'local', custom:false,
			displayOrder:25, fieldClass:null, wrapperClass:null, config: JsonOutput.toJson([credentialTypes:['api-key', 'username-password']]).toString()
		)
		optionTypes << new OptionType(
			code:"backupProviderType.backupTest.serviceToken", inputType:OptionType.InputType.PASSWORD, name:'password', category:"backupProviderType.backupTest",
			fieldName:'serviceToken', fieldCode:'gomorpheus.optiontype.ApiToken', fieldLabel:'API Token', fieldContext:'domain', fieldGroup:'default',
			required:false, enabled:true, requireOnCode:'credential.type:local', editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:30, fieldClass:null, localCredential:true
		)
		optionTypes << new OptionType(
			code:"backupProviderType.backupTest.username", inputType:OptionType.InputType.TEXT, name:'username', category:"backupProviderType.backupTest",
			fieldName:'username', fieldCode:'gomorpheus.optiontype.Username', fieldLabel:'Username', fieldContext:'domain', fieldGroup:'default',
			required:false, enabled:true, requireOnCode:'credential.type:local', editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:31, fieldClass:null, localCredential:true
		)
		optionTypes << new OptionType(
			code:"backupProviderType.backupTest.password", inputType:OptionType.InputType.PASSWORD, name:'password', category:"backupProviderType.backupTest",
			fieldName:'password', fieldCode:'gomorpheus.optiontype.Password', fieldLabel:'Password', fieldContext:'domain', fieldGroup:'default',
			required:false, enabled:true, requireOnCode:'credential.type:local', editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:32, fieldClass:null, localCredential:true
		)

		optionTypes << new OptionType(
			code:"backupProviderType.backupTest.overrideView", inputType:OptionType.InputType.CHECKBOX, name:'overrideView', category:"backupProviderType.backupTest",
			fieldName:'overrideView', fieldCode:'morpheusdata.backupTest.overrideView', fieldLabel:'Override Integration View', fieldContext:'config', fieldGroup:'default',
			required:false, enabled:true, editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:38, fieldClass:null
		)

		optionTypes << new OptionType(
			code:"backupProviderType.backupTest.input1", inputType:OptionType.InputType.TEXT, name:'input1', category:"backupProviderType.backupTest",
			fieldName:'input1', fieldCode:'morpheusdata.backupTest.inputOne', fieldLabel:'Input 1', fieldContext:'config', fieldGroup:'default',
			required:true, enabled:true, editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:40, fieldClass:null
		)

		optionTypes << new OptionType(
			code:"backupProviderType.backupTest.input2", inputType:OptionType.InputType.TEXT, name:'input2', category:"backupProviderType.backupTest",
			fieldName:'input2', fieldCode:'morpheusdata.backupTest.inputTwo', fieldLabel:'Input 2', fieldContext:'config', fieldGroup:'default',
			required:true, enabled:true, editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:45, fieldClass:null
		)

		return optionTypes
	}

	@Override
	Collection<OptionType> getReplicationGroupOptionTypes() {
		Collection<OptionType> optionTypes = new ArrayList()
		return optionTypes;
	}

	@Override
	Collection<OptionType> getReplicationOptionTypes() {
		Collection<OptionType> optionTypes = new ArrayList()
		return optionTypes;
	}

	@Override
	Collection<OptionType> getBackupJobOptionTypes() {
		Collection<OptionType> optionTypes = new ArrayList()
		return optionTypes;
	}

	@Override
	Collection<OptionType> getBackupOptionTypes() {
		Collection<OptionType> optionTypes = new ArrayList()

		optionTypes << new OptionType(
			code:'backup.backupTest.color', inputType:OptionType.InputType.SELECT, name:'color', optionSource:'colorOptions',
			category:'backup.backupTest', fieldName:'color', fieldCode: 'morpheusdata.backupTest.color', fieldLabel:'Color', fieldContext:'backup.config',
			required:false, enabled:true, editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:0, fieldClass:null
		)

		optionTypes << new OptionType(
			code:"backup.backupTest.foo", inputType:OptionType.InputType.TEXT, name:'foo', category:"backup.backupTest",
			fieldName:'foo', fieldCode: 'morpheusdata.backupTest.foo', fieldLabel:'Foo', fieldContext:'backup.config', fieldGroup:'default',
			required:true, enabled:true, editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:10, fieldClass:null
		)
		
		optionTypes << new OptionType(
			code:"backup.backupTest.numbers", inputType:OptionType.InputType.RADIO, name:'Numbers', category:"backup.backupTest", optionSource:"numberOptions",
			fieldName:'numbers', fieldCode: 'morpheusdata.backupTest.numbers', fieldLabel:'Numbers', fieldContext:'backup.config', fieldGroup:'default',
			required:false, enabled:true, editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:10, fieldClass:null, visibleOnCode: "backup.backupTest.color"
		)

		return optionTypes
	}

	@Override
	Collection<OptionType> getInstanceReplicationGroupOptionTypes() {
		Collection<OptionType> optionTypes = new ArrayList()
		return optionTypes;
	}


	@Override
	BackupJobProvider getBackupJobProvider() {
		new DefaultBackupJobProvider(getPlugin(), morpheus);
	}

	// provider
	@Override
	ServiceResponse configureBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map config, Map opts) {
		log.debug("config: ${config}")
		log.debug("opts: ${opts}")
		backupProviderModel.setConfigProperty("input1", opts.config?.input1)
		backupProviderModel.setConfigProperty("input2", opts.config?.input2)
		return ServiceResponse.success(backupProviderModel)
	}

	@Override
	ServiceResponse validateBackupProvider(BackupProviderModel backupProviderModel, Map opts) {
		return ServiceResponse.success(backupProviderModel)
	}

	@Override
	ServiceResponse deleteBackupProvider(BackupProviderModel backupProviderModel, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse refresh(BackupProviderModel backupProviderModel) {
		return ServiceResponse.success()
	}

	@Override

	@Override
	HTMLResponse renderTemplate(com.morpheusdata.model.BackupProvider backupProvider) {
		if(!backupProvider.getConfigProperty("overrideView")) {
			return null
		}
		ViewModel<com.morpheusdata.model.BackupProvider> model = new ViewModel<>()
		model.object = backupProvider

		return getRenderer().renderTemplate("hbs/backupTestProviderView", model);
	}

	/**
	 * {@inheritDoc}
	 */
	Renderer<?> getRenderer() {
		if(renderer == null) {
			renderer = new HandlebarsRenderer("renderer", getPlugin().getClassLoader());
			renderer.registerAssetHelper(getPlugin().getName());
			renderer.registerNonceHelper(getMorpheus().getWebRequest());
			renderer.registerI18nHelper(getPlugin(),getMorpheus());
		}
		return renderer;
	}
}
