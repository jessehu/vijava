/*================================================================================
Copyright (c) 2009 VMware, Inc. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, 
this list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice, 
this list of conditions and the following disclaimer in the documentation 
and/or other materials provided with the distribution.

* Neither the name of VMware, Inc. nor the names of its contributors may be used
to endorse or promote products derived from this software without specific prior 
written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL VMWARE, INC. OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.
================================================================================*/

package com.vmware.vim.rest;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;

public class RestManagedObject
{
  private RestClient rc = null;
  private String moid = null;
  private XPath xpath = null;
  
  public RestManagedObject(RestClient rc, String moid)
  {
    this.rc = rc;
    this.moid = moid;
    this.xpath = XPathFactory.newInstance().newXPath();
  }
  
  public String getPropertyDO(String path) throws IOException
  {
    if(path.length()==0)
    {
      return rc.get("moid=" + moid + "doPath=" + path);
    }
    else
    {
      return rc.get("moid=" + moid);
    }
  }
  
  public String getAllProperties() throws IOException
  {
    return rc.get("moid=" + moid);
  }

  public String getPropertyAsString(String path) throws IOException, XPathExpressionException
  {
    String propName = null;
    String doName = "";
    
    int last = path.lastIndexOf(".");
    if(last!=-1)
    {
      doName = path.substring(0, last);
      propName = path.substring(last+1);
    }else
    {
      propName = path;
    }
    
    String doXml = getPropertyDO(doName);
    
    xpath.reset();
    return xpath.evaluate("//" + propName + "/text()", new InputSource(new StringReader(doXml)));
  }
  
  public String invoke(String method, Map<String, String> para) throws Exception
  {
    return rc.post("moid=" + moid + "&method=" + method, para);        
  }
  
  public String invoke(String method) throws Exception
  {
    return rc.post("moid=" + moid + "&method=" + method);          
  }
}