/**
 *    Copyright 2009-2017 Wudao Software Studio(wudaosoft.com)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.wudaosoft.laodongbuzhu.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Changsoul Wu
 * 
 */
public class UserInfo {

	private String name;
	
	private String loginId;

	private String password;
	
	private String applyId;
	
	private String sessionId;

	private boolean isLogin;

	private boolean isFinish;
	
	private ApplyStatus applyStatus;
	
	private Map<String, Object> applyData;

	private List<ScheduledFuture<?>> futures;

	public UserInfo() {
		reset();
	}

	public UserInfo(String name, String loginId, String password) {
		this();
		this.name = name;
		this.loginId = loginId;
		this.password = password;
	}
	
	public void reset() {
		this.isLogin = false;
		this.isFinish = false;
		this.applyStatus = ApplyStatus.unsubmit;
		this.futures = new ArrayList<>();
		cancelAllScheduled();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	public ApplyStatus getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(ApplyStatus applyStatus) {
		this.applyStatus = applyStatus;
	}

	public Map<String, Object> getApplyData() {
		return applyData;
	}

	public void setApplyData(Map<String, Object> applyData) {
		this.applyData = applyData;
	}


	public List<ScheduledFuture<?>> getFutures() {

		return futures;
	}
	
	public UserInfo addFutures(ScheduledFuture<?> future) {

		futures.add(future);
		return this;
	}
	
	public void cancelAllScheduled() {
		
		for(ScheduledFuture<?> future : futures) {
			
			future.cancel(true);
		}
		
		if(!futures.isEmpty())
			futures.clear();
	}

	@Override
	public String toString() {
		return loginId;
	}

}
