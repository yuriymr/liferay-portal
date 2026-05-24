/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.cms.site.initializer.internal.display.context;

import com.liferay.depot.service.DepotEntryLocalService;
import com.liferay.headless.asset.library.resource.v1_0.AssetLibraryResource;
import com.liferay.object.constants.ObjectFolderConstants;
import com.liferay.object.model.ObjectEntryFolder;
import com.liferay.object.service.ObjectDefinitionService;
import com.liferay.object.service.ObjectDefinitionSettingLocalService;
import com.liferay.object.service.ObjectEntryFolderLocalService;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import com.liferay.site.cms.site.initializer.internal.util.ActionUtil;
import com.liferay.translation.exporter.TranslationInfoItemFieldValuesExporterRegistry;
import com.liferay.trash.TrashHelper;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * @author Yuri Monteiro
 */
public class ViewObjectsDisplayContext
	extends BaseSectionDisplayContext {

	public ViewObjectsDisplayContext(
		AssetLibraryResource.Factory assetLibraryResourceFactory,
		DepotEntryLocalService depotEntryLocalService, long groupId,
		GroupLocalService groupLocalService,
		HttpServletRequest httpServletRequest, Language language,
		ObjectDefinitionService objectDefinitionService,
		ObjectDefinitionSettingLocalService objectDefinitionSettingLocalService,
		ObjectEntryFolderLocalService objectEntryFolderLocalService,
		ModelResourcePermission<ObjectEntryFolder>
			objectEntryFolderModelResourcePermission,
		Portal portal,
		TranslationInfoItemFieldValuesExporterRegistry
			translationInfoItemFieldValuesExporterRegistry
		) {

		super(
			depotEntryLocalService, null, groupLocalService, httpServletRequest,
			language, objectDefinitionService,
			objectDefinitionSettingLocalService,
			objectEntryFolderModelResourcePermission, portal,
			translationInfoItemFieldValuesExporterRegistry);

		_objectEntryFolderLocalService = objectEntryFolderLocalService;
		_objectDefinitionService = objectDefinitionService;
	}

	public Map<String, Object> getEmptyState() {
		return HashMapBuilder.<String, Object>put(
			"description",
			LanguageUtil.get(httpServletRequest, "no-assets-yet")
		).put(
			"image", "/states/cms_empty_state_files.svg"
		).put(
			"title", LanguageUtil.get(httpServletRequest, "no-assets-yet")
		).build();
	}

	public Map<String, Object> getBreadcrumbProps() {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		if (objectEntryFolder == null) {
			addBreadcrumbItem(
				jsonArray, false, null,
				"Objects List");

			return HashMapBuilder.<String, Object>put(
				"breadcrumbItems", jsonArray
			).put(
				"hideSpace", true
			).build();
		}

		addBreadcrumbItem(
			jsonArray, false, ActionUtil.getObjectsSectionURL(themeDisplay),
			"Objects List");

		for (String objectEntryFolderId :
			StringUtil.split(
				objectEntryFolder.getTreePath(), CharPool.SLASH)) {

			ObjectEntryFolder parentObjectEntryFolder =
				_objectEntryFolderLocalService.fetchObjectEntryFolder(
					GetterUtil.getLong(objectEntryFolderId));

			if (parentObjectEntryFolder.getStatus() !=
				WorkflowConstants.STATUS_IN_TRASH) {

				continue;
			}

			if (objectEntryFolder.getObjectEntryFolderId() ==
				parentObjectEntryFolder.getObjectEntryFolderId()) {

				addBreadcrumbItem(
					jsonArray, true, null, parentObjectEntryFolder.getName());
			}
		}

		return HashMapBuilder.<String, Object>put(
			"breadcrumbItems", jsonArray
		).put(
			"displayType",
			() -> {
				Group group = groupLocalService.fetchGroup(
					objectEntryFolder.getGroupId());

				UnicodeProperties unicodeProperties =
					group.getTypeSettingsProperties();

				return GetterUtil.get(
					unicodeProperties.get("logoColor"), "outline-0");
			}
		).put(
			"size", "md"
		).build();
	}

	@Override
	protected String getCMSSectionFilterString() {

		long id = 0;

		try {
			id = _objectDefinitionService.getObjectDefinitionByExternalReferenceCode("L_CMS_TEAM", themeDisplay.getCompanyId()).getObjectDefinitionId();
		}
		catch (PortalException e) {
			throw new RuntimeException(e);
		}
		return appendStatus("cmsRoot eq true and objectDefinitionId eq " + id);
	}


	@Override
	protected String[] getObjectFolderExternalReferenceCodes() {
		return new String[] {
			ObjectFolderConstants.EXTERNAL_REFERENCE_CODE_CONTENT_STRUCTURES,
			ObjectFolderConstants.EXTERNAL_REFERENCE_CODE_FILE_TYPES
		};
	}

	@Override
	protected String getRootObjectEntryFolderExternalReferenceCode() {
		return null;
	}

	private final ObjectEntryFolderLocalService _objectEntryFolderLocalService;

	private final ObjectDefinitionService _objectDefinitionService;
}
