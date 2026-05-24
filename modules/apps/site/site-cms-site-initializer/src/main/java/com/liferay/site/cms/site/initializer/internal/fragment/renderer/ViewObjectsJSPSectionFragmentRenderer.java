package com.liferay.site.cms.site.initializer.internal.fragment.renderer;

import com.liferay.depot.service.DepotEntryLocalService;
import com.liferay.fragment.renderer.FragmentRenderer;
import com.liferay.headless.asset.library.resource.v1_0.AssetLibraryResource;
import com.liferay.object.model.ObjectEntryFolder;
import com.liferay.object.service.ObjectDefinitionService;
import com.liferay.object.service.ObjectDefinitionSettingLocalService;
import com.liferay.object.service.ObjectEntryFolderLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.site.cms.site.initializer.internal.display.context.ViewObjectsDisplayContext;

import jakarta.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Yuri Monteiro
 */
@Component(service = FragmentRenderer.class)
public class ViewObjectsJSPSectionFragmentRenderer
		extends BaseJSPSectionFragmentRenderer<ViewObjectsDisplayContext>{
	@Override
	public String getLabelKey() {
		return "recycle-bin";
	}

	@Override
	protected ViewObjectsDisplayContext getDisplayContext(
		HttpServletRequest httpServletRequest)
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return new ViewObjectsDisplayContext(
			_assetLibraryResourceFactory, _depotEntryLocalService,
			themeDisplay.getScopeGroupId(), groupLocalService,
			httpServletRequest, language, _objectDefinitionService,
			_objectDefinitionSettingLocalService,
			_objectEntryFolderLocalService,
			_objectEntryFolderModelResourcePermission, _portal,
			translationInfoItemFieldValuesExporterRegistry);
	}

	@Override
	protected String getJSPPath() {
		return "/view_objects.jsp";
	}

	@Reference
	private AssetLibraryResource.Factory _assetLibraryResourceFactory;

	@Reference
	private DepotEntryLocalService _depotEntryLocalService;

	@Reference
	private ObjectDefinitionService _objectDefinitionService;

	@Reference
	private ObjectDefinitionSettingLocalService
		_objectDefinitionSettingLocalService;

	@Reference
	private ObjectEntryFolderLocalService _objectEntryFolderLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.object.model.ObjectEntryFolder)"
	)
	private ModelResourcePermission<ObjectEntryFolder>
		_objectEntryFolderModelResourcePermission;

	@Reference
	private Portal _portal;

}
