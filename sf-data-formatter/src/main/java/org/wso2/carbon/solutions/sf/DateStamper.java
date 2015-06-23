/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.solutions.sf;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

import javax.xml.namespace.QName;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class DateStamper extends AbstractMediator {
    @Override
    public boolean mediate(MessageContext messageContext) {
        OMElement body = messageContext.getEnvelope().getBody();
        OMFactory omFac = body.getOMFactory();
        Iterator<OMElement> records = (Iterator<OMElement>) body.getFirstChildWithName(new QName("urn:partner.soap.sforce.com", "queryResponse")).
                getFirstChildWithName(new QName("urn:partner.soap.sforce.com", "result")).getChildrenWithLocalName("records");
        OMElement record, tqRecordDate;
        Date recordDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tqRecordDateStr = sdf.format(recordDate);

        while (records.hasNext()) {
            record = records.next();
            tqRecordDate = omFac.createOMElement(new QName("urn:sobject.partner.soap.sforce.com", "RecordDate","sf"));
            tqRecordDate.setText(tqRecordDateStr);
            record.addChild(tqRecordDate);
        }
        return true;
    }
}
