<%@ include file="/init.jsp" %>

<%
ViewObjectsDisplayContext viewObjectsDisplayContext =
	(ViewObjectsDisplayContext)request.getAttribute(
		ViewObjectsDisplayContext.class.getName());
%>

<div class="cms-section custom-empty-state">
	<div class="recycle-bin-section">
		<div>
			<react:component
				module="{RecycleBinToolbar} from site-cms-site-initializer"
				props="<%= viewObjectsDisplayContext.getBreadcrumbProps() %>"
			/>
		</div>

		<frontend-data-set:headless-display
			apiURL="<%= viewObjectsDisplayContext.getAPIURL() %>"
			emptyState="<%= viewObjectsDisplayContext.getEmptyState() %>"
			formName="fm"
			id="<%= CMSSiteInitializerFDSNames.LIST_OBJECTS_SECTION %>"
			itemsPerPage="20"
			selectedItemsKey="id"
			selectionType="multiple"
			style="fluid"
		/>
		</div>
</div>