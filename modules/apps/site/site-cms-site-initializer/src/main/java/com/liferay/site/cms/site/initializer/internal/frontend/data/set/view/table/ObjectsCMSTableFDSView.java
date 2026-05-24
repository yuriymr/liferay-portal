package com.liferay.site.cms.site.initializer.internal.frontend.data.set.view.table;

import com.liferay.frontend.data.set.view.FDSView;
import com.liferay.frontend.data.set.view.table.FDSTableSchema;
import com.liferay.frontend.data.set.view.table.FDSTableSchemaBuilder;
import com.liferay.frontend.data.set.view.table.FDSTableSchemaBuilderFactory;
import com.liferay.site.cms.site.initializer.internal.constants.CMSSiteInitializerFDSNames;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Locale;

@Component(
	property = "frontend.data.set.name=" + CMSSiteInitializerFDSNames.LIST_OBJECTS_SECTION,
	service = FDSView.class
)
public class ObjectsCMSTableFDSView extends BaseCMSTableFDSView {
	@Override
	public FDSTableSchema getFDSTableSchema(Locale locale) {
		FDSTableSchemaBuilder fdsTableSchemaBuilder =
			fdsTableSchemaBuilderFactory.create();

		return fdsTableSchemaBuilder.add(
			"embedded.title", "title",
			fdsTableSchemaField -> fdsTableSchemaField.setActionId(
				"actionLink"
			).setContentRenderer(
				"simpleActionLinkTableCellRenderer"
			).setSortable(
				true
			)
		).add(
			"embedded.systemProperties.objectDefinitionBrief.label", "type"
		).add(
			"embedded.scopeKey", "space",
			fdsTableSchemaField -> fdsTableSchemaField.setContentRenderer(
				"spaceTableCellRenderer")
		).build();
	}

	@Reference
	protected FDSTableSchemaBuilderFactory fdsTableSchemaBuilderFactory;
}
