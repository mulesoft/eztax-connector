/**
 * Mule Development Kit
 * Copyright 2010-2011 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This file was automatically generated by the Mule Development Kit
 */
package org.mule.modules;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.datacontract.schemas._2004._07.eztaxwebservice.AddressData;
import org.datacontract.schemas._2004._07.eztaxwebservice.TaxData;
import org.datacontract.schemas._2004._07.eztaxwebservice.Transaction;
import org.datacontract.schemas._2004._07.eztaxwebservice.ZipAddress;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.modules.api.EZTaxClient;

import ar.com.zauber.commons.mom.MapObjectMapper;
import ar.com.zauber.commons.mom.MapObjectMappers;
import ar.com.zauber.commons.mom.converter.TypeConverter;
import ar.com.zauber.commons.mom.style.impl.CXFStyle;

/**
 * Cloud Module
 *
 * @author gastonponti
 */
@Connector(name="eztax", schemaVersion="1.0-SNAPSHOT")
public class EZTaxModule
{
    /**
     * Configurable
     */
    @Configurable
    private String username;
    
    /**
     * Configurable
     */
    @Configurable
    private String password;
    
    private EZTaxClient client = null;
    
    private MapObjectMapper mom = MapObjectMappers.defaultWithPackage("org.datacontract.schemas._2004._07.eztaxwebservice")
                                                    .withSetterStyle(CXFStyle.STYLE)
                                                    .withConverter(new EnumToIntegerConverter())
                                                    .build();

    /**
     * This method accepts transaction data and performs appropriate tax calculations.
     *
     * {@sample.xml ../../../doc/EZTax-connector.xml.sample eztax:calculate-taxes}
     *
     * @param strategy Origination, Termination, and Bill To information is passed using this type.
     * @param A {@link Transaction}
     * @return An array of {@link TaxData} objects that contain the information about the taxes applied.
     */
    @Processor
    public List<TaxData> calculateTaxes(CalculationStrategyType strategy,
                                        Map<String, Object> transaction)
    {
        return client.calculateTaxes(strategy, (Transaction) mom.unmap(transaction, Transaction.class)).getTaxData();
    }
    
    /**
     * This method accepts transaction data and performs appropriate tax adjustment calculations.
     *
     * {@sample.xml ../../../doc/EZTax-connector.xml.sample eztax:calculate-adjustment}
     * 
     * @param strategy Origination, Termination, and Bill To information is passed using this type.
     * @param A {@link Transaction}
     * @return An array of {@link TaxData} objects that contain the information about the taxes applied.
     */
    @Processor
    public List<TaxData> calculateAdjustment(CalculationStrategyType strategy,
                                             Map<String, Object> transaction)
    {
        return client.calculateAdjustment(strategy, (Transaction) mom.unmap(transaction, Transaction.class)).getTaxData();
    }
    
    /**
     * This method returns the addresses for the specified jurisdiction. If the 
     * jurisdiction is invalid the return will be NULL.
     *
     * {@sample.xml ../../../doc/EZTax-connector.xml.sample eztax:get-address}
     * 
     * @param pCode The PCode for the desired jurisdiction.
     * @return A list of {@link AddressData} objects that contain the addresses 
     *         for the jurisdiction specified by the supplied PCode.
     */
    @Processor
    public List<AddressData> getAddress(Long pCode)
    {
        return client.getAddress(pCode).getAddressData();
    }

    /**
     * This method returns the tax category for the specified tax type. If the tax 
     * type is invalid the return will be NULL or an empty string.
     *
     * {@sample.xml ../../../doc/EZTax-connector.xml.sample eztax:get-tax-category}
     * 
     * @param taxCode The tax type.
     * @return A string describing the tax category for the specified tax type.
     */
    @Processor
    public String getTaxCategory(Integer taxCode)
    {
        return client.getTaxCategory(taxCode);
    }
    
    /**
     * This method returns the description for the specified tax type. If the tax 
     * type is invalid the return will be NULL or an empty string.
     *
     * {@sample.xml ../../../doc/EZTax-connector.xml.sample eztax:get-tax-description}
     * 
     * @param taxCode The tax type.
     * @return A string describing the specified tax type.
     */
    @Processor
    public String getTaxDescription(Integer taxCode)
    {
        return client.getTaxDescription(taxCode);
    }
    
    /**
     * This method returns the PCode for the specified FIPS code. If the FIPS Code 
     * is invalid or has no EZTax jurisdiction the return will be NULL.
     *
     * {@sample.xml ../../../doc/EZTax-connector.xml.sample eztax:convert-fips-to-pcode}
     * 
     * @param fips The FIPS code.
     * @return The PCode.
     */
    @Processor
    public Long convertFipsToPcode(String fips)
    {
        return client.convertFipsToPcode(fips);
    }
    
    /**
     * This method returns the PCode for the specified NpaNxx value. If the NpaNxx 
     * value is invalid or has no EZTax jurisdiction the return will be NULL.
     *
     * {@sample.xml ../../../doc/EZTax-connector.xml.sample eztax:convert-npanxx-to-pcode}
     * 
     * @param npanxxCode The NpaNxx value.
     * @return The PCode.
     */
    @Processor
    public Long convertNpanxxToPcode(Long npanxxCode)
    {
        return client.convertNpanxxToPcode(npanxxCode);
    }

    /**
     * This method returns the PCode for the specified ZIP code. If the ZIP code 
     * is invalid or has no EZTax jurisdiction the return will be NULL.
     *
     * {@sample.xml ../../../doc/EZTax-connector.xml.sample eztax:convert-zip-to-pcode}
     * 
     * @param zipCode The address Zip code
     * @return The PCode.
     */
    @Processor
    public Long convertZipToPcode(String zipCode)
    {
        ZipAddress zipAddress = new ZipAddress();
        zipAddress.setZipCode(zipCode);
        
        return client.convertZipToPcode(zipAddress);
    }
    
    /**
     * This method returns the time on the EZTax Web Service.
     *
     * {@sample.xml ../../../doc/EZTax-connector.xml.sample eztax:get-server-time}
     * 
     * @return The EZTax Web Service server time.
     */
    @Processor
    public Date getServerTime()
    {
        return new Date(client.getServerTime().getMillisecond());
    }

    /**
     * Returns the username.
     * 
     * @return  with the username.
     */
    
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username. 
     *
     * @param username  with the username.
     */
    
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Returns the password.
     * 
     * @return  with the password.
     */
    
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets the password. 
     *
     * @param password  with the password.
     */
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    /**
     * @author Gaston Ponti
     * @since Dec 2, 2011
     */
    private final class EnumToIntegerConverter implements TypeConverter
    {
        @Override
        public <A> A convert(Object value, Class<A> destinationType)
        {
            EZTaxEnums enum_ = (EZTaxEnums) value;
            if(destinationType == Integer.class)
                return (A)((Integer)enum_.getValue());
            else if (destinationType == Short.class)
                return (A)((Short) ((short) enum_.getValue()));
            else
                throw new IllegalArgumentException();
        }
    
        @Override
        public boolean canConvert(Class<?> sourceType, Class<?> destinationType)
        {
            return sourceType.isEnum() && (isIntegral(destinationType));
        }
    
        protected boolean isIntegral(Class<?> destinationType)
        {
            return destinationType == Integer.class || destinationType == Short.class;
        }
    }
}