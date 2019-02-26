package cn.com.bjggs.system.service.Impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import cn.com.bjggs.Constant;
import cn.com.bjggs.core.domain.TreeNode;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.core.util.InvokeUtil;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.system.domain.SysCode;
import cn.com.bjggs.system.domain.SysDept;
import cn.com.bjggs.system.domain.SysHouses;
import cn.com.bjggs.system.domain.SysMenu;
import cn.com.bjggs.system.domain.SysRole;
import cn.com.bjggs.system.domain.SysUser;
import cn.com.bjggs.system.enums.TypePageOpen;
import cn.com.bjggs.system.enums.TypeUser;
import cn.com.bjggs.system.service.ISystemService;
import cn.com.bjggs.system.util.PassUtil;

@IocBean(name = "systemService", args = { "refer:dao" })
public class SystemServiceImpl extends BaseServiceImpl implements
		ISystemService {

	public SystemServiceImpl(Dao dao) {
		this.dao = dao;
	}

	public SysUser loginUser(String loginName, String loginPass) {
		if (Strings.isBlank(loginName) || Strings.isBlank(loginPass))
			return null;
		Criteria cri = Cnd.cri();
		cri.where()
				.andEquals("loginName", loginName)
				.andEquals("loginPass",
						PassUtil.genSysUserPass(loginName, loginPass));
		SysUser user = fetch(SysUser.class, cri, false);
		return user;
	}

	public SysUser initPass(SysUser user) {
		String newPass = PassUtil.genSysUserPass(user.getLoginName(),
				user.getLoginPass());
		user.setLoginPass(newPass);
		user = update(user);
		return user;
	}

	public Collection<SysMenu> getUserMenus(SysUser user) {
		Collection<SysMenu> menus = new LinkedHashSet<SysMenu>();
		// 系统管理员admin
		if (user.getType() == TypeUser.SYS.val()) {
			Map<String, TypeOrder> ordersMap = new LinkedHashMap<String, TypeOrder>();
			ordersMap.put("level", TypeOrder.ASC);
			ordersMap.put("seqno", TypeOrder.ASC);
			List<SysMenu> allMenus = findList(SysMenu.class, null, ordersMap);
			if (!Lang.isEmpty(allMenus)) {
				menus.addAll(allMenus);
			}
			return menus;
		}
		// 其他根据菜单权限查询所有角色/根据角色 刷新菜单
		Collection<SysRole> roles = findLinks(user, "roles", "id").getRoles();
		for (SysRole r : roles) {
			if (r.getStatus() == 1) {
				menus.addAll(findLinks(r, "menus", "id").getMenus());
			}
		}
		List<SysMenu> menuList = new LinkedList<SysMenu>();
		menuList.addAll(menus);
		Collections.sort(menuList, new Comparator<SysMenu>(){
			public int compare(SysMenu o1, SysMenu o2) {
				return o1.getLevel() == o2.getLevel() ? o1.getSeqno() - o2.getSeqno() : o1.getLevel() - o2.getLevel();
			}
		});
		return menuList;
	}

	/**
	 * 这一步 替换ID PID的原因
	 */
	public <T> T saveNextLevel(Class<T> clazz, T obj) {
		if (obj == null)
			throw new RuntimeException("未找到对应的信息!");
		Object id = InvokeUtil.getValue(obj, "id");
		if (Strings.isBlank((String) id)
				|| ParseUtil.toLong(id, Constant.ID_DEFAULT) == Constant.ID_DEFAULT)
			throw new RuntimeException("主键值不存在，无法添加下级信息!");
		if (checkUnique(clazz, "code",
				(String) InvokeUtil.getValue(obj, "code"), null))
			throw new RuntimeException("编码已存在!".intern());
		InvokeUtil.setValue(obj, "id", null);
		InvokeUtil.setValue(obj, "pid", id);
		return update(obj);
	}

	public <T> int updateStatus(Class<T> clazz, String id, int status) {
		return this.dao.update(clazz,
				Chain.make("status", status == 0 ? 0 : 1),
				Cnd.where("id", "=", id));
	}

	public <T> int updateCodeStatus(Class<T> clazz, String type, String code,
			int status) {
		return this.dao.update(clazz,
				Chain.make("status", status == 0 ? 0 : 1),
				Cnd.where("type", "=", type).and("code", "=", code));
	}

	public Map<String, Integer> updateCodeStatus(String[] cids, int status) {
		status = status == 0 ? 0 : 1;
		this.dao.update(SysCode.class, Chain.make("status", status),
				Cnd.where("id", "in", cids));

		List<SysCode> codes = query(SysCode.class, Cnd.where("id", "=", cids)
				.and("status", "=", status));

		Map<String, Integer> typesMap = new LinkedHashMap<String, Integer>();
		String type = null;

		for (SysCode c : codes) {
			type = c.getType();
			if (!Strings.isBlank(type)) {
				if (typesMap.containsKey(type)) {
					typesMap.put(type, typesMap.get(type) + 1);
				} else {
					typesMap.put(type, 1);
				}
			}
		}
		return typesMap;
	}

	public int deleteCodes(String[] cids) {
		return delete(SysCode.class,
				Cnd.where("id", "in", cids).and("status", "=", 1));
	}

	public SysUser updateUserAndRoles(SysUser u, String oldPass, String roleIds) {
		if (!Strings.isBlank(roleIds)) {
			String[] rids = roleIds.indexOf(",") != -1 ? Strings
					.splitIgnoreBlank(roleIds, ",") : new String[] { roleIds };
			List<SysRole> roles = query(SysRole.class,
					Cnd.where("id", "in", rids));
			u.setRoles(roles);
		} else {
			u.setRoles(null);
		}
		if (!u.getLoginPass().equals(oldPass)) {
			u.setLoginPass(PassUtil.genSysUserPass(u.getLoginName(),
					u.getLoginPass()));
		}
		return (SysUser) updateBaseAndLinks(u, "roles");
	}

	public SysRole updateRoleAndMenus(SysRole r, String menuIds) {
		if (!Strings.isBlank(menuIds)) {
			String[] mids = menuIds.indexOf(",") != -1 ? Strings
					.splitIgnoreBlank(menuIds, ",") : new String[] { menuIds };
			List<SysMenu> menus = query(SysMenu.class,
					Cnd.where("id", "in", mids));
			r.setMenus(menus);
		} else {
			r.setMenus(null);
		}
		return (SysRole) updateBaseAndLinks(r, "menus");
	}

	public void deleteDept(String id) {
		List<String> deptIds = new LinkedList<String>();
		findChildIdsByPid(SysDept.class, deptIds, id);
		if (!Lang.isEmpty(deptIds)) {
			for (String deptId : deptIds) {
				deleteDept(deptId);
			}
		}
		//删除本部门角色
		List<String> roleIds = findIds(SysRole.class, "deptId", id);
		deleteBaseAndLinks(SysRole.class, roleIds, "menus");
		//删除所有下级部门
		delete(SysDept.class, deptIds);
		//删除本部门
		delete(SysDept.class, id);
	}

	public <T> List<String> deleteSelfAndChilds(Class<T> clazz, String id) {
		List<String> ids = new LinkedList<String>();
		findChildIdsByPid(clazz, ids, id);
		if(!Lang.isEmpty(ids)){
			for(String cid : ids){
				deleteSelfAndChilds(clazz, cid);
			}
		}
		delete(clazz, id);
		return ids;
	}

	public void updateTreeSameLevelSeqno(Class<?> clazz, String sortedIds) {
		if (Strings.isBlank(sortedIds) || sortedIds.indexOf(",") == -1)
			return;
		String[] ids = Strings.splitIgnoreBlank(sortedIds.trim(), ",");
		Object object = null;
		int i = 0;
		for (int len = ids.length; i < len; i++) {
			object = fetch(clazz, ids[i], false);
			if (object != null) {
				InvokeUtil.setValue(object, "seqno", i + 1);
				update(object);
			}
		}
	}

	public <T> T genSysEntryCode(Class<T> clazz, String pid, int level) {
		String code = "";
		int codeLen = Math.max(PropsUtil.getInteger("sys.code.len"), 2);
		String codePad = Strings.sBlank(PropsUtil.getString("sys.code.pad"),
				"0");

		Criteria cri = Cnd.cri();
		cri.where().andEquals("pid", pid);
		int maxCode = this.dao.func(clazz, "max", "code", cri);
		if (maxCode == 0) {
			if (level == 0) {
				code = Strings.alignRight(maxCode + 1, (level + 1) * codeLen,
						codePad.charAt(0));
			} else {
				T obj = fetch(clazz, pid, false);
				if (obj == null) {
					throw new RuntimeException("上级信息不存在!");
				}
				String num = Strings.alignRight(Character.valueOf('1'),
						codeLen, codePad.charAt(0));
				code = InvokeUtil.getValue(obj, "code") + num;
			}
		} else {
			code = Strings.alignRight(Integer.valueOf(maxCode + 1), (level + 1)
					* codeLen, codePad.charAt(0));
		}
		T target = InvokeUtil.newInstance(clazz);
		InvokeUtil.setValue(target, "code", code);
		InvokeUtil.setValue(target, "pid", pid);
		return target;
	}

	public String buildDWZMenuTreeHTML(SysUser user, String contextPath) {
		Collection<SysMenu> menus = getUserMenus(user);
		return buildDWZMenuTreeHTML(menus, contextPath);
	}

	public String buildDWZMenuTreeHTML(Collection<SysMenu> menus,
			String contextPath) {
		Collection<TreeNode> nodes = buildTreeStructure(SysMenu.class, menus);

		StringBuffer html = new StringBuffer();

		boolean isAccordionMode = PropsUtil.getBoolean("sys.menu.accordion");
		if (isAccordionMode) {
			generateAccordionTreeHTML(nodes, 1, contextPath, html);
			return html.toString();
		}
		generateSimpleTreeHTML(nodes, 1, contextPath, html);

		StringBuffer wrapHTML = new StringBuffer("<div class=\"accordionHeader\"><h2><span>Folder</span>系统菜单</h2></div><div class=\"accordionContent\"><ul class=\"tree treeFolder\">");
		wrapHTML.append(html.toString()).append("</ul></div>");
		return wrapHTML.toString();
	}

	private void generateAccordionTreeHTML(Collection<TreeNode> nodes,
			int level, String contextPath, StringBuffer html) {
		if (Lang.isEmpty(nodes) || level < 1)
			return;

		for (TreeNode node : nodes) {
			if (node.isValid()) {

				List<TreeNode> childs = node.getChilds();

				html.append("\n").append(Strings.dup(' ', (level - 1) * 8));
				if (level == 1) {
					html.append("<div class=\"accordionHeader\">")
							.append("<h2><span>Folder</span>")
							.append(node.getText()).append("</h2></div>")
							.append("<div class=\"accordionContent\">")
							.append("<ul class=\"tree treeFolder\">");
				} else {
					html.append("<li>").append("<a");
					String url = node.getLink();

					if (!Strings.isBlank(url)) {
						int render = node.getRender();
						if (TypePageOpen.FRAME.val() == render) {
							html.append(" target=\"navTab\" external=\"true\" ");
						} else if (TypePageOpen.BLANK.val() == render) {
							html.append(" target=\"_blank\" ");
						} else {
							html.append(" target=\"navTab\" ");
						}
						html.append(" rel=\"page_")
								.append(node.getValue())
								.append("\" href=\"")
								.append(contextPath)
								.append(url)
								.append(url.indexOf("?") != -1 ? "&tid="
										: "?tid=").append(node.getValue())
								.append("\"");
					}
					html.append(">").append(node.getText()).append("</a>");
					if (Lang.isEmpty(childs)) {
						html.append("</li>");
					}
				}

				if (!Lang.isEmpty(childs)) {
					html.append("\n").append(Strings.dup(' ', level * 4));
					if (level > 1) {
						html.append("<ul>");
					}
					generateAccordionTreeHTML(node.getChilds(), level + 1,
							contextPath, html);
					html.append("\n").append(Strings.dup(' ', level * 4));
					if (level > 1) {
						html.append("</ul>").append("</li>");
					}
				}
				if (level == 1) {
					html.append("\n</ul>").append("</div>");
				}
			}
		}
	}

	private void generateSimpleTreeHTML(Collection<TreeNode> nodes, int level,
			String contextPath, StringBuffer html) {
		if (Lang.isEmpty(nodes) || level < 1)
			return;
		for (TreeNode node : nodes) {
			if (node.isValid()) {
				List<TreeNode> childs = node.getChilds();
				html.append("\n").append(Strings.dup(' ', (level - 1) * 8));
				html.append("<li>").append("<a");

				String url = node.getLink();
				if (!Strings.isBlank(url)) {
					int render = node.getRender();
					if (TypePageOpen.FRAME.val() == render) {
						html.append(" target=\"navTab\" external=\"true\" ");
					} else if (TypePageOpen.BLANK.val() == render) {
						html.append(" target=\"_blank\" ");
					} else {
						html.append(" target=\"navTab\" ");
					}
					html.append(" rel=\"page_").append(node.getValue())
							.append("\" href=\"").append(contextPath)
							.append(url)
							.append(url.indexOf("?") != -1 ? "&tid=" : "?tid=")
							.append(node.getValue()).append("\"");
				}
				html.append(">").append(node.getText()).append("</a>");
				if (level > 1 && Lang.isEmpty(childs)) {
					html.append("</li>");
				}

				if (!Lang.isEmpty(childs)) {
					html.append("\n").append(Strings.dup(' ', level * 4));
					html.append("<ul>");

					generateSimpleTreeHTML(node.getChilds(), level + 1,
							contextPath, html);

					html.append("\n").append(Strings.dup(' ', level * 4));

					html.append("</ul>").append("</li>");
				}
			}
		}
	}
	
	public void savePowerHouses(String[] houses, String uid){
		this.delete(SysHouses.class, Cnd.where("uid", "=", uid));
		List<SysHouses> list = new LinkedList<SysHouses>();
		if(houses != null){
			for(String s : houses){
				list.add(new SysHouses(s, uid));
			}
			//修改用户标记
			dao.update(SysUser.class, Chain.make("allStatus", 0), Cnd.where("id", "=", uid));
		} else {
			//修改用户标记
			dao.update(SysUser.class, Chain.make("allStatus", 1), Cnd.where("id", "=", uid));
		}
		dao.insert(list);
	}
}
