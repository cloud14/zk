<%--
listfooter.dsp

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Fri Jan 13 13:01:30     2006, Created by tomyeh@potix.com
}}IS_NOTE

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
--%><%@ taglib uri="/WEB-INF/tld/web/core.dsp.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/zk/core.dsp.tld" prefix="z" %>
<c:set var="self" value="${requestScope.arg.self}"/>
<td id="${self.uuid}"${self.outerAttrs}${self.innerAttrs}>${self.imgTag}<c:out value="${self.label}"/><c:forEach var="child" items="${self.children}">
	${z:redraw(child, null)}
</c:forEach></td>
