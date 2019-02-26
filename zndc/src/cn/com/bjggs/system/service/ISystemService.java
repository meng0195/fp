package cn.com.bjggs.system.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.com.bjggs.core.service.IBaseService;
import cn.com.bjggs.system.domain.SysMenu;
import cn.com.bjggs.system.domain.SysRole;
import cn.com.bjggs.system.domain.SysUser;

public interface ISystemService extends IBaseService{

	public abstract SysUser loginUser(String paramString1, String paramString2);

	public abstract SysUser initPass(SysUser paramSysUser);

	public abstract Collection<SysMenu> getUserMenus(SysUser paramSysUser);

	public abstract <T> int updateStatus(Class<T> paramClass, String paramString, int paramInt);

	public abstract Map<String, Integer> updateCodeStatus(String[] paramArrayOfString, int paramInt);

	public abstract int deleteCodes(String[] paramArrayOfString);

	public abstract SysUser updateUserAndRoles(SysUser paramSysUser, String paramString1, String paramString2);

	public abstract SysRole updateRoleAndMenus(SysRole paramSysRole, String paramString);

	public abstract <T> T saveNextLevel(Class<T> paramClass, T paramT) throws Exception;

	public abstract void deleteDept(String paramString);

	public abstract <T> List<String> deleteSelfAndChilds(Class<T> paramClass, String paramString);

	public abstract void updateTreeSameLevelSeqno(Class<?> paramClass, String paramString) throws Exception;

	public abstract <T> T genSysEntryCode(Class<T> paramClass, String paramString, int paramInt);

	public abstract String buildDWZMenuTreeHTML(SysUser paramSysUser, String paramString);

	public abstract String buildDWZMenuTreeHTML(Collection<SysMenu> paramCollection, String paramString);
	
	public abstract void savePowerHouses(String[] houses, String uid);
}
