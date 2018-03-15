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

/**
 * @author Changsoul Wu
 * 
 */
public class LoginInfo {

	private String _1_;
	private String desKey;
	private String ticket;
	private String subsys;

	public String get_1_() {
		return _1_;
	}

	public void set_1_(String _1_) {
		this._1_ = _1_;
	}

	public String getDesKey() {
		return desKey;
	}

	public void setDesKey(String desKey) {
		this.desKey = desKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getSubsys() {
		return subsys;
	}

	public void setSubsys(String subsys) {
		this.subsys = subsys;
	}

	@Override
	public String toString() {
		return "LoginInfo [_1_=" + _1_ + ", desKey=" + desKey + ", ticket=" + ticket + ", subsys=" + subsys + "]";
	}
}
