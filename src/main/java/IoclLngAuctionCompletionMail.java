
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

import org.apache.commons.lang3.StringUtils;

//import mstc.REVMail;

public class IoclLngAuctionCompletionMail {
	
	/*
	public static String getBidderCountryOfOrigin(String vendorid,String item_id,String compnent_id,String event_id,Connection con,Map<String,String> MasterParamvaluemap,String originalValueOfP4,String originalValueOfP6) {
		String traceMessage="";
		traceMessage +="getBidderCountryOfOrigin inputs vendorid :"+vendorid+" item_id :"+item_id+" compnent_id :"+compnent_id+" event_id :"+event_id+" originalValueOfP4 :"+originalValueOfP4+" originalValueOfP6 :"+originalValueOfP6;
		try{
			int itemgot=Integer.parseInt(item_id.trim());
			String HEADER_NAME="COUNTRYORIGIN";
			if(itemgot==-1){
				HEADER_NAME="COUNTRYORIGINCOMBINATION";
			}
			String IOCL_LNG_COUNTRY_DUTY_REF_ID="0";
		
		  
			String query = "SELECT BID_AMOUNT FROM EPROC.IOCL_LNG_PRICE_BID WHERE EVENT_ID=? AND ITEM_ID=? AND COMPONENT_ID=? AND VENDOR_ID=? AND HEADER_NAME='"+HEADER_NAME+"'";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1,event_id);
			pstmt.setString(2,item_id);
			pstmt.setString(3,compnent_id);
			pstmt.setString(4,vendorid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				IOCL_LNG_COUNTRY_DUTY_REF_ID = rs.getString(1);
				traceMessage +="Bidder Bid IOCL_LNG_COUNTRY_DUTY_REF_ID "+IOCL_LNG_COUNTRY_DUTY_REF_ID;
			}
			rs.close();
			pstmt.close();
			
			int country_id=Integer.parseInt(IOCL_LNG_COUNTRY_DUTY_REF_ID.trim());
			
			if(country_id >0){
				String SQL="SELECT DUTY,SURCHARGE,CD_FORMULA FROM EPROC.IOCL_LNG_COUNTRY_DUTY WHERE REF_ID=?";
				PreparedStatement stmt=con.prepareStatement(SQL);
				stmt.setString(1,country_id+"");
				rs=stmt.executeQuery();
				if(rs.next()){
					double CustomDutyRate=(rs.getDouble(1)/100);
					double SocialWelfareSurcharge=(rs.getDouble(2)/100);
					String CD_FORMULA=rs.getString(3);
					traceMessage +="\nCountry Wise CustomDutyRate "+CustomDutyRate;
					traceMessage +="\nCountry Wise SocialWelfareSurcharge "+SocialWelfareSurcharge;
					traceMessage +="\nCountry Wise CD_FORMULA "+CD_FORMULA;
					MasterParamvaluemap.put("P4",CustomDutyRate+"");
					MasterParamvaluemap.put("P6",SocialWelfareSurcharge+"");
					MasterParamvaluemap.put("CD",CD_FORMULA);
					traceMessage +="\ngetBidderCountryOfOrigin Updated MasterParamvaluemap country_id >0 "+MasterParamvaluemap;
				}
				rs.close();
				stmt.close();
			}else{
				MasterParamvaluemap.put("P4",originalValueOfP4);
				MasterParamvaluemap.put("P6",originalValueOfP6);
				traceMessage +="\ngetBidderCountryOfOrigin Updated MasterParamvaluemap country_id = 0 "+MasterParamvaluemap;
			}
		}catch(Exception ff){
			System.out.println("Error Calculating LP "+ff);
			traceMessage +="\ngetBidderCountryOfOrigin Error  "+ff;
		}
		return traceMessage;

	}
	*/
	public static String getBidderCountryOfOriginMailUtils(String vendorid,String item_id,String compnent_id,String event_id,Connection con,Map<String,String> MasterParamvaluemap,String originalValueOfP4,String originalValueOfP6) {
		String traceMessage="";
		try{
			int itemgot=Integer.parseInt(item_id.trim());
			String HEADER_NAME="COUNTRYORIGIN";
			if(itemgot==-1){
				HEADER_NAME="COUNTRYORIGINCOMBINATION";
			}
			String IOCL_LNG_COUNTRY_DUTY_REF_ID="0";
		
		  
			String query = "SELECT BID_AMOUNT FROM EPROC.IOCL_LNG_PRICE_BID WHERE EVENT_ID=? AND ITEM_ID=? AND COMPONENT_ID=? AND VENDOR_ID=? AND HEADER_NAME='"+HEADER_NAME+"'";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1,event_id);
			pstmt.setString(2,item_id);
			pstmt.setString(3,compnent_id);
			pstmt.setString(4,vendorid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				IOCL_LNG_COUNTRY_DUTY_REF_ID = rs.getString(1);
				traceMessage +="Bidder Bid IOCL_LNG_COUNTRY_DUTY_REF_ID "+IOCL_LNG_COUNTRY_DUTY_REF_ID;
			}
			rs.close();
			pstmt.close();
			
			int country_id=Integer.parseInt(IOCL_LNG_COUNTRY_DUTY_REF_ID.trim());

			String SQL="SELECT FORMULA  FROM EPROC.IOCL_LNG_BID_PRODUCT_FORMULA WHERE FORMULA_NOMENCLATURE='CDF' AND PRODUCT_ID IN(SELECT PRODUCT_ID FROM IOCL_LNG_TENDER_SNAPSHOT WHERE EVENT_ID = ?)";
			pstmt=con.prepareStatement(SQL);
			pstmt.setString(1,event_id);
			rs=pstmt.executeQuery();
			if(rs.next()){
				MasterParamvaluemap.put("CDF",rs.getString(1));
			}
			rs.close();
			pstmt.close();

			SQL="SELECT FORMULA  FROM EPROC.IOCL_LNG_BID_PRODUCT_FORMULA WHERE FORMULA_NOMENCLATURE='CD' AND PRODUCT_ID IN(SELECT PRODUCT_ID FROM IOCL_LNG_TENDER_SNAPSHOT WHERE EVENT_ID = ?)";
			pstmt=con.prepareStatement(SQL);
			pstmt.setString(1,event_id);
			rs=pstmt.executeQuery();
			if(rs.next()){
				MasterParamvaluemap.put("CD",rs.getString(1));
			}
			rs.close();
			pstmt.close();

			if(country_id >0){
				SQL="SELECT DUTY,SURCHARGE,CD_FORMULA FROM EPROC.IOCL_LNG_COUNTRY_DUTY WHERE REF_ID=?";
				PreparedStatement stmt=con.prepareStatement(SQL);
				stmt.setString(1,country_id+"");
				rs=stmt.executeQuery();
				if(rs.next()){
					double CustomDutyRate=(rs.getDouble(1)/100);
					double SocialWelfareSurcharge=(rs.getDouble(2)/100);
					String CD_FORMULA=rs.getString(3);
					traceMessage +="\nCountry Wise CustomDutyRate "+CustomDutyRate;
					traceMessage +="\nCountry Wise SocialWelfareSurcharge "+SocialWelfareSurcharge;
					traceMessage +="\nCountry Wise CD_FORMULA "+CD_FORMULA;
					MasterParamvaluemap.put("P4",CustomDutyRate+"");
					MasterParamvaluemap.put("P6",SocialWelfareSurcharge+"");
					MasterParamvaluemap.put("CD",CD_FORMULA);
					traceMessage +="\ngetBidderCountryOfOrigin Updated MasterParamvaluemap country_id >0 "+MasterParamvaluemap;
				}
				rs.close();
				stmt.close();
			}else{
				MasterParamvaluemap.put("P4",originalValueOfP4);
				MasterParamvaluemap.put("P6",originalValueOfP6);
				traceMessage +="\ngetBidderCountryOfOrigin Updated MasterParamvaluemap country_id = 0 "+MasterParamvaluemap;
			}
		}catch(Exception ff){
			System.out.println("Error Calculating LP "+ff);
			traceMessage +="\ngetBidderCountryOfOrigin Error  "+ff;
		}
		return traceMessage;

	}//function end

	public static Map<String, String> getDisport(Connection con, String principal_ref_id) throws Exception {
		Map<String, String> dispIdNameMap = new HashMap<String, String>();
		try {
			String query = "SELECT PORT_ID,DISCHARGE_PORT FROM EPROC.IOCL_LNG_DISPORT WHERE PRINCIPAL_REF_ID = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, principal_ref_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				dispIdNameMap.put(rs.getString(1), rs.getString(2));
			}
			rs.close();
			pstmt.close();
		} catch (Exception ex) {
			System.out.println("Exception in getDisport: " + ex);
		}
		return dispIdNameMap;
	}

	public static Map<String, String> getUnit(Connection con, String principal_ref_id) throws Exception {
		Map<String, String> unitIdNameMap = new LinkedHashMap<String, String>();
		try {
			String query = "SELECT UNIT_ID, UNIT_NAME FROM EPROC.IOCL_LNG_UNIT_MASTER WHERE PRINCIPAL_REF_ID = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, principal_ref_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				unitIdNameMap.put(rs.getString(1), rs.getString(2));
			}
			rs.close();
			pstmt.close();
		} catch (Exception ex) {
			System.out.println("Exception in getUnit: " + ex);
		}
		return unitIdNameMap;
	}

	public static Map<String, String> getRefinery(Connection con, String principal_ref_id) throws Exception {
		Map<String, String> refinIdNameMap = new LinkedHashMap<String, String>();
		try {
			String query = "SELECT REF_CUST_ID,REF_CUST_NAME FROM EPROC.IOCL_LNG_REFCUST WHERE PRINCIPAL_REF_ID = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, principal_ref_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				refinIdNameMap.put(rs.getString(1), rs.getString(2));
			}
			rs.close();
			pstmt.close();
		} catch (Exception ex) {
			System.out.println("Exception in getRefinery: " + ex);
		}
		return refinIdNameMap;
	}

	public static String removeMultiSpace(String inputString) {
		StringBuffer retString = new StringBuffer();
		String[] parts = inputString.split("\\s{2,}");
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			part = part.trim();
			if (i != 0)
				retString.append(" ");
			retString.append(part);
		}
		return retString.toString();
	}

	public static Map<String, String> getFormulaMasterMap(Connection con, String principal_ref_id, String productId, String tenderID) throws Exception {
		Map<String, String> formulaMasterMap = new LinkedHashMap<String, String>();
		try {
			// GET ALL FORMULAS
			String query = "SELECT BID_PRODUCT_FORMULA_IDS FROM IOCL_LNG_TENDER_SNAPSHOT WHERE EVENT_ID = ? AND PRINCIPAL_REF_ID = ?";
			String bid_product_formula_ids = "";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, tenderID);
			pstmt.setString(2, principal_ref_id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				bid_product_formula_ids = rs.getString(1);
			}
			rs.close();
			pstmt.close();

			query = "SELECT FORMULA_NOMENCLATURE,FORMULA FROM EPROC.IOCL_LNG_BID_PRODUCT_FORMULA WHERE PRINCIPAL_REF_ID = ? AND REF_ID IN("+ bid_product_formula_ids +")";

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, principal_ref_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getString(2) != null && !"".equals(rs.getString(2))) {
					formulaMasterMap.put(rs.getString(1), rs.getString(2));
				}
			}
			rs.close();
			pstmt.close();

		} catch (Exception ex) {
			System.out.println("Exception in getFormulaMasterMap: " + ex);
		}
		return formulaMasterMap;
	}

	public static Map<String, String> getParamValueMasterMap(String eventid, String productId, String principal_ref_id,	Connection con) throws Exception {
		Map<String, String> Paramvaluemap = new LinkedHashMap<String, String>();
		try {
			ResultSet rs = null;
			Statement stmt = con.createStatement();
			String SQL = "SELECT HEADER_CODE, HEADER_UNIT, HEDAER_VALUE  FROM EPROC.IOCL_LNG_ANNEXURE1_P WHERE EVENT_ID="+ eventid + " AND STATUS='a' AND FREEZE = 'y'";
			rs = stmt.executeQuery(SQL);
			while (rs.next()) {
				int unit = rs.getInt(2);
				double hedaer_value = 0.00;
				String header_code = "";
				if (unit == 1) {// for %
					hedaer_value = rs.getDouble(3);
					hedaer_value = (hedaer_value / 100);
					header_code = rs.getString(1);
					Paramvaluemap.put(header_code, hedaer_value + "");
				} else {
					hedaer_value = rs.getDouble(3);
					header_code = rs.getString(1);
					Paramvaluemap.put(header_code, hedaer_value + "");
				}
			} // while end
			rs.close();
			// x and exchange rate required for c1
			SQL = "SELECT HEADER_CODE,COALESCE(HEADER_VALUE,0.00)  FROM EPROC.IOCL_LNG_ANNEXURE1_Q WHERE EVENT_ID="	+ eventid + " AND STATUS='a' AND FREEZE = 'y'";
			rs = stmt.executeQuery(SQL);
			while (rs.next()) {
				double hedaer_value = 0.00;
				String header_code = "";
				hedaer_value = rs.getDouble(2);
				header_code = rs.getString(1);
				if (header_code.equals("Q1"))
					header_code = "X";
				if (header_code.equals("Q2"))
					header_code = "ER";
				Paramvaluemap.put(header_code, hedaer_value + "");
			} // while end
			rs.close();
			stmt.close();
		} catch (Exception hh) {
			System.out.println("Error Caught getParamValueMasterMap " + hh);
		}
		return Paramvaluemap;
	}

	public static int getCargoCountItemwise(String itemId, Connection con) throws Exception {
		int cargoCount = 0;
		try {
			String getCarCnt = "SELECT COUNT(*) FROM EPROC.IOCL_LNG_INDENT_CARGO_CONFIG WHERE STATUS = 'a' AND ITEM_ID = ?";
			PreparedStatement ps = con.prepareStatement(getCarCnt);
			ps.setString(1, itemId);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				cargoCount = rs.getInt(1);
			rs.close();
			ps.close();
		} catch (Exception ex) {
			System.out.println("Exception in getCargoCountItemwise: " + ex);
		}
		return cargoCount;
	}

	public static int getRefCountItemwise(String itemId, Connection con) throws Exception {
		int refinCount = 0;
		try {
			String getCarCnt = "SELECT COUNT(*) FROM EPROC.IOCL_LNG_INDENT_REFINERY WHERE STATUS = 'a' AND ITEM_ID = ?";
			PreparedStatement ps = con.prepareStatement(getCarCnt);
			ps.setString(1, itemId);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				refinCount = rs.getInt(1);
			rs.close();
			ps.close();
		} catch (Exception ex) {
			System.out.println("Exception in getRefCountItemwise: " + ex);
		}
		return refinCount;
	}

	public static double getEvaluatedValueForSpecFormula(Connection con, String principal_ref_id, String formulaNom, Map<String, String> paramValMap, Map<String, String> formulaMasterMap) throws Exception {
		double evaluatedValue = 0.00;
		try {
			List<Map<String, String>> listOfMaps = getFormulaMapRecc(con, principal_ref_id, paramValMap, formulaNom, formulaMasterMap);
			evaluatedValue = evaluateFormula(formulaMasterMap.get(formulaNom), listOfMaps.get(1));
		} catch (Exception ex) {
			System.out.println("Exception in getEvaluatedValueForSpecFormula: " + ex);
		}
		return evaluatedValue;
	}

	public static List<String> getFormulaIngreds(String formula) throws Exception {
		List<String> formulaIngreds = new ArrayList<String>();
		if (formula != null) {
			try {
				String reg = "\\s*[^a-zA-Z0-9.]+";
				String formulaHere = formula.replaceAll("^[\\s()]+|[\\s()]+|[\\s{}]+|[\\s{}]+|[\\s\\[]+|[\\s\\]]+$","");
				String[] res = formulaHere.split(reg);
				List<String> list = new ArrayList(Arrays.asList(res));
				HashSet<String> set = new HashSet(Arrays.asList(res));
				formulaIngreds.addAll(set);
			} catch (Exception ex) {
				System.out.println("Exception in getFormulaIngreds: " + ex);
			}
		}
		return formulaIngreds;
	}

	public static double evaluateFormula(String formula, Map<String, String> paramMap) throws Exception {
		double result = 0.00;
		try {
			if (formula != null) {
				String reg = "\\s*[^a-zA-Z0-9.]+";
				String formulaHere = formula.replaceAll("^[\\s()]+|[\\s()]+|[\\s{}]+|[\\s{}]+|[\\s\\[]+|[\\s\\]]+$","");
				String[] res = formulaHere.split(reg);
				HashSet<String> set = new HashSet(Arrays.asList(res));
				Map<String, String> PARAMVALUEMAP = new LinkedHashMap<String, String>();
				for (String val : set) {
					if (!paramMap.containsKey(val)) {
						String paramvalstr = paramMap.get(val);
						try {
							Double.parseDouble(val);
						} catch (Exception ss) {
							if (val != null && !"".equals(val)) {
								if (paramvalstr == null || "".equals(paramvalstr))
									paramvalstr = "0.00";
								PARAMVALUEMAP.put(val, paramvalstr);
							}
						}
					} else {
						PARAMVALUEMAP.put(val, paramMap.get(val));
					}
				}
				try {
					Set<String> keys = PARAMVALUEMAP.keySet();
					ExpressionBuilder exb = new ExpressionBuilder(formula);
					exb.variables(keys);
					Expression ex = exb.build();
					Iterator<String> itr = keys.iterator();
					while (itr.hasNext()) {
						String paramKey = itr.next();
						ex.setVariable(paramKey, Double.parseDouble(PARAMVALUEMAP.get(paramKey)));
					}
					result = ex.evaluate();
				} catch (UnknownFunctionOrVariableException e) {
					System.out.println("evaluateFormula ExpressionBuilder UnknownFunctionOrVariableException for the formula: "	+ formula + " : " + e);
				} catch (Exception e) {
					System.out.println("evaluateFormula ExpressionBuilder Exception for the formula: " + formula + " : " + e);
				}
			}
		} catch (Exception ex) {
			System.out.println("evaluateFormula ExpressionBuilder Outer Exception : " + ex);
		}
		return result;
	}

	public static List<Map<String, String>> getFormulaMapRecc(Connection con, String principal_ref_id, Map<String, String> paramValMapIn, String formulaNom, Map<String, String> formulaMasterMap) throws Exception {

		Map<String, String> formulaMap = new LinkedHashMap<String, String>();
		List<Map<String, String>> listOfMaps = new ArrayList<Map<String, String>>();
		try {
			Map<String, String> paramValMap = new LinkedHashMap<String, String>();
			paramValMap.putAll(paramValMapIn);
			Set<String> paramSet = paramValMap.keySet();

			if (formulaMasterMap.containsKey(formulaNom)) {
				List<String> formulaIngreds = getFormulaIngreds(formulaMasterMap.get(formulaNom));

				for (int i = 0; i < formulaIngreds.size(); i++) {
					String locFormulaNom = formulaIngreds.get(i);
					if (!paramValMap.containsKey(locFormulaNom)) {
						try {
							Double.parseDouble(locFormulaNom);
						} catch (Exception ee) {
							String formulaHere = formulaMasterMap.get(locFormulaNom);
							if (formulaHere == null) {
								paramValMap.put(locFormulaNom, "0.00");
							} else {
								boolean isTailingCond = false;
								List<String> formulaIngredsLoc = getFormulaIngreds(formulaHere);
								HashSet<String> formulaIngredSet = new HashSet(formulaIngredsLoc);

								while (!isTailingCond) {
									HashSet<String> setToCheck = new HashSet();
									for (String val : formulaIngredSet) {
										try {
											double dVal = Double.parseDouble(val);
										} catch (Exception ex) {
											if (val != null && !"".equals(val)) {
												setToCheck.add(val);
											}
										}
									}
									if (paramSet.containsAll(setToCheck)) {
										isTailingCond = true;
										try {
											if (!paramValMap.containsKey(locFormulaNom)) {
												double result = evaluateFormula(formulaHere, paramValMap);
												paramValMap.put(locFormulaNom, result + "");
												// formulaMap.remove(formulaNomLoc);
												paramValMap.put(locFormulaNom, result + "");
												if (!formulaMap.containsKey(locFormulaNom)) {
													formulaMap.put(locFormulaNom, formulaHere);
												}
											}
										} catch (Exception v) {
											System.out.println("getFormulaMapRecc Excption in evaluate : "+isTailingCond);
										}
										break;
									} else {
										if (!paramValMap.containsKey(locFormulaNom)) {
											formulaMap.put(locFormulaNom, formulaHere);
											List<Map<String, String>> mapList = getFormulaMapRecc(con, principal_ref_id,paramValMap, locFormulaNom, formulaMasterMap);
											Map<String, String> formulaMapLoc = mapList.get(0);
											Map<String, String> evalMapLoc = mapList.get(1);
											formulaMap.putAll(formulaMapLoc);
											paramValMap.putAll(evalMapLoc);
										} else
											break;
									}
								}
							}
						}
					}
				}
			}
			listOfMaps.add(formulaMap);
			listOfMaps.add(paramValMap);
		} catch (Exception ex) {
			System.out.println("Exception in getFormulaMap: " + ex);
		}
		return listOfMaps;
	}

	public static Map<String, String> getGnR(Connection con, String principal_ref_id, String event_id, String itemId, String refineryId) throws Exception {
		Map<String, String> GnRMap = new LinkedHashMap<String, String>();
		String gVal = "0.00";
		try {

			String disport_id = "0";
			String query = "SELECT DISTINCT DISPORT_IDS FROM EPROC.IOCL_LNG_INDENT_DISPORT WHERE INDENT_ID IN (SELECT INDENT_ID FROM IOCL_LNG_INDENT_MASTER WHERE EVENT_ID = ? AND PRINCIPAL_REF_ID = ?) AND ITEM_ID = ? AND STATUS = 'a'";
			if ("-1".equals(itemId)) {
				query = "SELECT DISTINCT DISPORT_IDS FROM EPROC.IOCL_LNG_INDENT_DISPORT WHERE INDENT_ID IN (SELECT INDENT_ID FROM IOCL_LNG_INDENT_MASTER WHERE EVENT_ID = ? AND PRINCIPAL_REF_ID = ?) AND STATUS = 'a'";
			}

			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, event_id);
			pstmt.setString(2, principal_ref_id);
			if (!("-1".equals(itemId))) {
				pstmt.setString(3, itemId);
			}

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				disport_id += "," + rs.getString(1);
			}
			rs.close();
			pstmt.close();
			query = "SELECT MAX(TARIFF_VALUE) FROM EPROC.IOCL_LNG_ANNEXURE3_G WHERE PRINCIPAL_REF_ID = ? AND EVENT_ID = ? AND REFINERY_ID = ? AND DIS_PORT_ID IN("+ disport_id + ") AND STATUS = 'a' AND FREEZE = 'y'";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, principal_ref_id);
			pstmt.setString(2, event_id);
			pstmt.setString(3, refineryId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				gVal = rs.getString(1);
				GnRMap.put("G", gVal);
			}
			rs.close();
			pstmt.close();

			query = "SELECT COALESCE(PERCENT_GST_PROD,0) FROM EPROC.IOCL_LNG_ANNEXURE2_R WHERE EVENT_ID=? AND STATUS='a' AND PRINCIPAL_REF_ID = ? AND REFINERY_ID = ? AND FREEZE = 'y'";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, event_id);
			pstmt.setString(2, principal_ref_id);
			pstmt.setString(3, refineryId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				double rValPercent = rs.getDouble(1);
				double rVal = rValPercent / 100;
				GnRMap.put("R", rVal + "");
			}
			rs.close();
			pstmt.close();

		} catch (Exception ex) {
			System.out.println("Exception in GnRMap: " + ex);
		}
		return GnRMap;
	}

	public static Map<String, String> getVendorSpecificParamMap(Connection con, String principal_ref_id,
			String event_id, String itemId, String stage) throws Exception {
		Map<String, String> vendorSpecificParamMap = new LinkedHashMap<String, String>();
		try {
			// GET L1 VENDOR AND M,C,C1,DP
			String M_VALUE = "0.00", C_VALUE = "0.00", C1_VALUE = "0.00", DP_VALUE = "0.00", vendorId = "", raStat = "",
					Y = "", REF_ID = "", C1_RAW = "0.00";
			String query = "";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String minlp = "0.00";
			boolean istender = true;
			if ("AFTER_RA".equals(stage)) {
				istender = false;
			}
			if ("COUNTER".equals(stage)) {
				istender = false;
			}
			if (istender) {
				query = "SELECT M, C, C1, DP, VENDOR_ID, RA_STAT,Y,REF_ID,CARGO,C1_RAW FROM EPROC.IOCL_LNG_POST_BID_RANK WHERE EVENT_ID = ? AND ITEM_ID = ? AND RANK_NO = '1' ORDER BY LP, DP, Y, M, C, C1, REF_ID";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, event_id);
				pstmt.setString(2, itemId);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					M_VALUE = rs.getString(1);
					C_VALUE = rs.getString(2);
					C1_VALUE = rs.getString(3);
					DP_VALUE = rs.getString(4);
					vendorId = rs.getString(5);
					raStat = rs.getString(6);
					Y = rs.getString(7);
					REF_ID = rs.getString(8);
					C1_RAW = rs.getString(10);
				}
				rs.close();
				pstmt.close();
			}

			if ("AFTER_RA".equals(stage)) {
				// OVERWRITING CODE RA
				try {
					query = "SELECT MIN(LP) FROM EPROC.IOCL_LNG_RA_LAST_BID WHERE EVENT_ID=? AND ITEM_ID=?";
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, event_id);
					pstmt.setString(2, itemId);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						minlp = rs.getString(1);
					}
					rs.close();
					pstmt.close();

					String ranref_id = "0";
					String M_C_C1 = "";
					String RA_BA = "";
					String RA_Y = "";
					String RA_DP = "";
					String RA_L1_VENDOR = "";
					query = "SELECT RANK_REF_ID,BID_AMOUNT,M_C_C1,Y,DP,LP,VENDOR_ID FROM EPROC.IOCL_LNG_RA_LAST_BID WHERE EVENT_ID=? AND ITEM_ID=? AND LP="+minlp;
					PreparedStatement ps1 = con.prepareStatement(query);
					ps1.setString(1, event_id);
					ps1.setString(2, itemId);
					//ps1.setString(3, minlp);
					ResultSet rs1 = ps1.executeQuery();
					if (rs1.next()) {
						ranref_id = rs1.getString(1);
						RA_BA = rs1.getString(2);
						M_C_C1 = rs1.getString(3);
						RA_Y = rs1.getString(4);
						RA_DP = rs1.getString(5);
						RA_L1_VENDOR = rs1.getString(7);
					}
					rs1.close();
					ps1.close();
					if ((!REF_ID.equals(ranref_id) || REF_ID.equals(ranref_id)) && !ranref_id.equals("0")) {
						query = "SELECT CARGO,ITEM_ID,PRODUCT_ID,REF_ID,DEL_TERM_ID,VENDOR_ID,M,C,C1,Y,DP FROM IOCL_LNG_POST_BID_RANK WHERE REF_ID=?";
						ps1 = con.prepareStatement(query);
						ps1.setString(1, ranref_id);
						rs1 = ps1.executeQuery();
						if (rs1.next()) {
							if (M_C_C1.equals("C"))
								C_VALUE = RA_BA;
							if (M_C_C1.equals("M"))
								M_VALUE = RA_BA;
							if (M_C_C1.equals("C1"))
								C1_VALUE = RA_BA;
							DP_VALUE = RA_DP;
							Y = RA_Y;
							vendorId = RA_L1_VENDOR;
						}
						rs1.close();
						ps1.close();
					}
				} catch (Exception reverse) {
					System.out.println("Error from RA " + reverse);
				}

			}

			if ("COUNTER".equals(stage)) {
				// OVERWRITING CODE COUNTER
				try {
					query = "SELECT MIN(LP) FROM EPROC.IOCL_LNG_COUNTER_LAST_BID WHERE EVENT_ID=? AND ITEM_ID=?";
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, event_id);
					pstmt.setString(2, itemId);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						minlp = rs.getString(1);
					}
					rs.close();
					pstmt.close();

					String ranref_id = "0";
					String M_C_C1 = "";
					String CA_BA = "";
					String CA_Y = "";
					String CA_DP = "";
					String COUNTER_VENDOR = "";
					query = "SELECT RANK_REF_ID,BID_AMOUNT,M_C_C1,Y,DP,LP,VENDOR_ID FROM EPROC.IOCL_LNG_COUNTER_LAST_BID WHERE EVENT_ID=? AND ITEM_ID=? AND LP="+minlp;
					PreparedStatement ps1 = con.prepareStatement(query);
					ps1.setString(1, event_id);
					ps1.setString(2, itemId);
					//ps1.setString(3, minlp);
					ResultSet rs1 = ps1.executeQuery();
					if (rs1.next()) {
						ranref_id = rs1.getString(1);
						CA_BA = rs1.getString(2);
						M_C_C1 = rs1.getString(3);
						CA_Y = rs1.getString(4);
						CA_DP = rs1.getString(5);
						COUNTER_VENDOR = rs1.getString(7);
					}
					rs1.close();
					ps1.close();
					if ((!REF_ID.equals(ranref_id) || REF_ID.equals(ranref_id)) && !ranref_id.equals("0")) {
						query = "SELECT CARGO,ITEM_ID,PRODUCT_ID,REF_ID,DEL_TERM_ID,VENDOR_ID,M,C,C1,Y,DP FROM IOCL_LNG_POST_BID_RANK WHERE REF_ID=?";
						ps1 = con.prepareStatement(query);
						ps1.setString(1, ranref_id);
						rs1 = ps1.executeQuery();
						if (rs1.next()) {

							if (M_C_C1.equals("C"))
								C_VALUE = CA_BA;
							if (M_C_C1.equals("M"))
								M_VALUE = CA_BA;
							if (M_C_C1.equals("C1"))
								C1_VALUE = CA_BA;
							DP_VALUE = CA_DP;
							Y = CA_Y;
							vendorId = COUNTER_VENDOR;
						}
						rs1.close();
						ps1.close();
					}
				} catch (Exception reverse) {
					System.out.println("Error from RA " + reverse);
				}

			}
			vendorSpecificParamMap.put("M", M_VALUE);
			vendorSpecificParamMap.put("C", C_VALUE);
			vendorSpecificParamMap.put("C1", C1_VALUE);
			vendorSpecificParamMap.put("C1_RAW", C1_RAW);
			vendorSpecificParamMap.put("DP", DP_VALUE);
			vendorSpecificParamMap.put("Y", Y);
			vendorSpecificParamMap.put("Vendorid", vendorId);
			vendorSpecificParamMap.put("Event_id", event_id);

			// CHECK IF MSPA YES/NO
			String mspa_config = "";
			String getmspaConfig = "SELECT MSPA_CONFIG FROM IOCL_LNG_PRODUCT_CONFIG WHERE REF_ID IN (SELECT PRODUCT_ID FROM IOCL_LNG_TENDER_PROPERTIES WHERE EVENT_ID = ?) AND PRINCIPAL_REF_ID = ?";
			PreparedStatement psTenderTab = con.prepareStatement(getmspaConfig);
			psTenderTab.setString(1, event_id);
			psTenderTab.setString(2, principal_ref_id);
			ResultSet rsTenderTab = psTenderTab.executeQuery();
			if (rsTenderTab.next()) {
				mspa_config = rsTenderTab.getString(1);
			}
			rsTenderTab.close();
			psTenderTab.close();

			String prod_mspa_yes_ids = "";

			getmspaConfig = "SELECT PROD_MSPA_YES_ID FROM IOCL_LNG_TENDER_SNAPSHOT WHERE EVENT_ID=?";
			psTenderTab = con.prepareStatement(getmspaConfig);
			psTenderTab.setString(1, event_id);
			rsTenderTab = psTenderTab.executeQuery();
			if (rsTenderTab.next()) {
				prod_mspa_yes_ids = rsTenderTab.getString(1);
			}
			rsTenderTab.close();
			psTenderTab.close();

			String SQL1 = "SELECT coalesce(BID_AMOUNT,0.00) FROM EPROC.IOCL_LNG_PRICE_BID WHERE EVENT_ID =" + event_id+ " AND VENDOR_ID=" + vendorId + " AND HEADER_NAME='CREDITDAYS'";
			psTenderTab = con.prepareStatement(SQL1);
			rs = psTenderTab.executeQuery();
			// get data from IOCL LNG PRICE BID
			if (rs.next()) {
				vendorSpecificParamMap.put("MS6", (Double.parseDouble(rs.getString(1)) * 0.01) + "");
			}
			rs.close();

			// MSx MS1 MS4 MS6
			if ("y".equals(mspa_config)) { // MSPA YES
				String SQL = "SELECT PCG_LIMIT,SBLC_LIMIT,SBLC_VALUE,VENDOR_ID FROM EPROC.IOCL_LNG_MSPA_CONFIG WHERE REF_ID IN("
						+ prod_mspa_yes_ids + ") AND VENDOR_ID = " + vendorId;

				psTenderTab = con.prepareStatement(SQL);
				rs = psTenderTab.executeQuery();
				if (rs.next()) {
					double hedaer_value = 0.00;
					String header_code = "";
					String MS1 = rs.getString(1);
					String MS4 = rs.getString(2);
					String vendor_id = rs.getString(4); // COUNTERPARTY
					vendorSpecificParamMap.put("MS1", MS1 + "");
					vendorSpecificParamMap.put("MS4", MS4 + "");

				} // while end
				rs.close();
				psTenderTab.close();
			} else {
				vendorSpecificParamMap.put("MS1", "0.00");
				vendorSpecificParamMap.put("MS4", "0.00");
			}

			// GET MAXT: MAX(T6,T7): Deemed Cargo Quantity Preffered Value (T6) & Deemed Cargo Quantity Deviation (T7) CHECK DEVIATION YES OR NO AND BASIS VALUE OR PERCENT
			getmspaConfig = "SELECT PREFERRED_FROM, PREFERRED_TO, TOLERANCE_FROM, TOLERANCE_TO, DEVIATION_ALLOWED, DEVIATION_FROM, DEVIATION_TO, BASIS FROM IOCL_LNG_TENDER_TABLE WHERE EVENT_ID=? AND QUANTITY_TYPE NOT IN ('Cargo Size/ Volume Required') AND ITEM_ID = "
					+ itemId;
			if ("-1".equals(itemId)) {
				getmspaConfig = "SELECT PREFERRED_FROM, PREFERRED_TO, TOLERANCE_FROM, TOLERANCE_TO, DEVIATION_ALLOWED, DEVIATION_FROM, DEVIATION_TO, BASIS FROM IOCL_LNG_TENDER_TABLE WHERE EVENT_ID=? AND QUANTITY_TYPE NOT IN ('Cargo Size/ Volume Required')";
			}

			psTenderTab = con.prepareStatement(getmspaConfig);
			psTenderTab.setString(1, event_id);
			// psTenderTab.setString(2,itemId);
			rsTenderTab = psTenderTab.executeQuery();
			if (rsTenderTab.next()) {
				String deviation = rsTenderTab.getString(5);
				String basis = rsTenderTab.getString(8);
				String deempreferredval_from = rsTenderTab.getString(1);
				String deempreferredval_to = rsTenderTab.getString(2);
				String deemptolval_from = rsTenderTab.getString(3);
				String deemptolval_to = rsTenderTab.getString(4);
				String deempdevval_from = rsTenderTab.getString(6);
				String deempdevval_to = rsTenderTab.getString(7);

				if ("n".equalsIgnoreCase(deviation)) {
					if ("v".equalsIgnoreCase(basis)) {
						vendorSpecificParamMap.put("MAXT", deempreferredval_to);
					} else if ("p".equalsIgnoreCase(basis)) {
						double maxVal = Double.parseDouble(deempreferredval_from)
								+ Double.parseDouble(deempreferredval_from) * Double.parseDouble(deemptolval_to) / 100;
						vendorSpecificParamMap.put("MAXT", maxVal + "");
					}
				} else if ("y".equalsIgnoreCase(deviation)) {
					if ("v".equalsIgnoreCase(basis)) {
						vendorSpecificParamMap.put("MAXT", deempdevval_to);
					} else if ("p".equalsIgnoreCase(basis)) {
						double maxVal = Double.parseDouble(deempdevval_to)
								+ Double.parseDouble(deempdevval_to) * Double.parseDouble(deemptolval_to) / 100;
						vendorSpecificParamMap.put("MAXT", maxVal + "");
					}
				}
			}
			rsTenderTab.close();
			psTenderTab.close();
		} catch (Exception ex) {
			System.out.println("Exception in getVendorSpecificParamMap: " + ex);
		}
		return vendorSpecificParamMap;
	}


	public static String getFvalueCargoWise(String eventid, String principal_ref_id, Connection con, String cargoid)
			throws Exception {
		String fValue = "0.00";
		try {
			ResultSet rs = null;
			Statement stmt = con.createStatement();
			String SQL = "SELECT HEADER_CODE, PRICE, CARGO_ID  FROM EPROC.IOCL_LNG_ANNEXURE1_F WHERE EVENT_ID="	+ eventid + " AND STATUS='a' AND FREEZE = 'y' AND CARGO_ID = '" + cargoid		+ "' AND HEADER_CODE = 'F'";
			rs = stmt.executeQuery(SQL);
			if (rs.next()) {
				String cargo_id = rs.getString(3);
				String header_code = rs.getString(1);
				fValue = rs.getString(2);
			} // while end
			rs.close();
		} catch (Exception hh) {
			System.out.println("Error Caught getFvalueCargoWise " + hh);
		}
		return fValue;
	}

	public static String getERvalueCargoWise(String eventid, String principal_ref_id, Connection con, String itemid) throws Exception {
		String erValue = "0.00";
		try {
			ResultSet rs = null;
			Statement stmt = con.createStatement();
			String SQL = "SELECT RHQ_EXCH_RATE FROM EPROC.IOCL_LNG_ANNEXURE2_E WHERE EVENT_ID=" + eventid+ " AND STATUS='a' AND FREEZE = 'y' AND ITEM_ID = " + itemid;
			rs = stmt.executeQuery(SQL);
			if (rs.next()) {
				erValue = rs.getString(1);
			} // while end
			rs.close();
		} catch (Exception hh) {
			System.out.println("Error Caught getERvalueCargoWise " + hh);
		}
		return erValue;
	}

	public static double CalculateDPValue(String eventid, String productId, String principal_ref_id, Map Paramvaluemap,  String vendorid, String deltrmid, Connection con, boolean MSPAYES) throws Exception {
		String DP = "";
		try {
			ResultSet rs = null;
			Statement stmt = con.createStatement();

			// first get biddrs selected delivery term then formula
			// getbiddersY
			double Yval = CalculateYValue(eventid, productId, principal_ref_id, Paramvaluemap, vendorid, con, MSPAYES);
			Paramvaluemap.put("Y", Yval + "");
			// String SQL="SELECT FORMULA FROM IOCL_LNG_DELIVERY_TERM WHERE
			// REF_ID="+deltrmid;
			String dl_terms_config_ids = "";
			String SQL = "SELECT DL_TERMS_CONFIG_IDS FROM IOCL_LNG_TENDER_SNAPSHOT WHERE event_id=" + eventid+ " AND PRINCIPAL_REF_ID=" + principal_ref_id;

			rs = stmt.executeQuery(SQL);
			if (rs.next()) {
				dl_terms_config_ids = rs.getString(1);
			}
			rs.close();

			SQL = "SELECT b.FORMULA FROM IOCL_LNG_DELIVERY_TERM a, IOCL_LNG_DELIVERY_TERM_CONFIG b WHERE a.REF_ID="+ deltrmid + " AND b.DELIVERY_TERM_ID=a.REF_ID AND a.PRINCIPAL_REF_ID=" + principal_ref_id+ " AND a.PRINCIPAL_REF_ID=b.PRINCIPAL_REF_ID AND b.REF_ID IN (" + dl_terms_config_ids + ")";

			rs = stmt.executeQuery(SQL);
			if (rs.next()) {
				DP = rs.getString(1);
				DP = DP.toUpperCase();
			}
			rs.close();

		} catch (Exception ff) {
			System.out.println("Error Calculating DP " + ff);
		}
		return evaluateCustomFormula(DP, Paramvaluemap, vendorid, MSPAYES);
	}

	public static double CalculateYValue(String eventid, String productId, String principal_ref_id, Map Paramvaluemap, String vendorid, Connection con, boolean MSPAYES) throws Exception {
		String Y = "";
		try {
			ResultSet rs = null;
			Statement stmt = con.createStatement();
			String SQL = "SELECT C.FORMULA FROM IOCL_LNG_TENDER_PROPERTIES A,IOCL_LNG_PRODUCT_FORMULA_CONFIG B,IOCL_LNG_FORMULA_MASTER C WHERE A.EVENT_ID="	+ eventid + " AND A.FORMULA_CONFIG_ID=B.REF_ID AND B.FORMULA_ID=C.REF_ID";
			rs = stmt.executeQuery(SQL);
			if (rs.next()) {
				Y = rs.getString(1);
				Y = Y.toUpperCase();
			}
			rs.close();
		} catch (Exception ff) {
			System.out.println("Error Calculating Y " + ff);
		}
		return evaluateCustomFormula(Y, Paramvaluemap, vendorid, MSPAYES);
	}// function end

	public static double evaluateCustomFormula(String formula, Map<String, String> paramMap, String vendorid, boolean MSPAYES) throws Exception {
		double result = 0.00;
		try {
			String reg = "\\s*[^a-zA-Z0-9.]+";
			String str = formula;
			str = str.replaceAll("^[\\s()]+|[\\s()]+|[\\s{}]+|[\\s{}]+|[\\s\\[]+|[\\s\\]]+$", "");
			String[] res = str.split(reg);
			List<String> list = new ArrayList(Arrays.asList(res));
			HashSet<String> set = new HashSet(Arrays.asList(res));
			Map<String, String> PARAMVALUEMAP = new LinkedHashMap<String, String>();
			for (String val : set) {
				String paramvalstr = paramMap.get(val);
				if (val.startsWith("M") && !val.equals("MX") && MSPAYES) {
					paramvalstr = paramMap.get((val));
				}
				if (val.startsWith("M") && !val.equals("MX") && !MSPAYES) {
					paramvalstr = paramMap.get((val));
				}
				if (val.equals("MX")) {
					paramvalstr = Double.parseDouble(paramMap.get("M")) * Double.parseDouble(paramMap.get("X")) * 0.01+ "";
				}
				try {
					double t = Double.parseDouble(val);
				} catch (Exception ss) {
					PARAMVALUEMAP.put(val, paramvalstr);
				}
			}
			// out.println("<br/>PARAMVALUEMAP: "+PARAMVALUEMAP);
			try {
				Set<String> keys = PARAMVALUEMAP.keySet();
				ExpressionBuilder exb = new ExpressionBuilder(formula);
				exb.variables(keys);
				Expression ex = exb.build();
				Iterator<String> itr = keys.iterator();
				while (itr.hasNext()) {
					String paramKey = itr.next();
					// out.println("<br/>paramKey: "+paramKey);
					ex.setVariable(paramKey, Double.parseDouble(PARAMVALUEMAP.get(paramKey)));
				}
				result = ex.evaluate();
			} catch (UnknownFunctionOrVariableException e) {
				System.out.println("evaluateFormula ExpressionBuilder UnknownFunctionOrVariableException : " + e.getMessage());
			} catch (Exception e) {
				System.out.println("evaluateFormula ExpressionBuilder Exception : " + e.getMessage());
			}
		} catch (Exception ex) {
			System.out.println("evaluateFormula ExpressionBuilder Outer Exception : " + ex.getMessage());
		}
		return result;
	}

	public static Map<String, String> getBestOffer(String eventId, String cargo, String principal_ref_id, Connection con) throws Exception {
		String bestOffer = "";
		ResultSet rs = null;
		DecimalFormat df2 = new DecimalFormat("#0.00");
		PreparedStatement pstmt = null;
		Map<String, String> offerMap = new LinkedHashMap<String, String>();
		try {
			String mfilled = "";
			String cfilled = "";
			String c1filled = "";
			String formulaQuery = "SELECT M, C, C1 FROM EPROC.IOCL_LNG_PRODUCT_FORMULA_CONFIG WHERE REF_ID IN (SELECT FORMULA_CONFIG_ID FROM EPROC.IOCL_LNG_TENDER_PROPERTIES WHERE EVENT_ID = ? AND PRINCIPAL_REF_ID = ?) AND PRINCIPAL_REF_ID = ?";
			PreparedStatement ntpstmt = con.prepareStatement(formulaQuery);
			ntpstmt.setString(1, eventId);
			ntpstmt.setString(2, principal_ref_id);
			ntpstmt.setString(3, principal_ref_id);
			ResultSet ntrs = ntpstmt.executeQuery();
			if (ntrs.next()) {
				mfilled = ntrs.getString(1);
				cfilled = ntrs.getString(2);
				c1filled = ntrs.getString(3);
			}
			ntrs.close();
			ntpstmt.close();

			String prodUnitName = "";
			String unitQry = "SELECT u.UNIT_NAME FROM IOCL_LNG_UNIT_MASTER u, IOCL_LNG_TENDER_PROPERTIES t WHERE t.EVENT_ID=? and t.UNIT_PRODUCT_ID=u.UNIT_ID AND t.PRINCIPAL_REF_ID=? and t.PRINCIPAL_REF_ID=u.PRINCIPAL_REF_ID";
			pstmt = con.prepareStatement(unitQry);
			pstmt.setString(1, eventId);
			pstmt.setString(2, principal_ref_id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				prodUnitName = rs.getString(1);
			}
			rs.close();
			pstmt.close();

			// GET OFFER FROM TENDER
			String tenderOfferRefId = "";
			String Y = "", M = "", C = "", C1_RAW = "", DP = "";
			String getBestOfferQry = "SELECT Y,REF_ID,M,C,C1_RAW,DP FROM IOCL_LNG_POST_BID_RANK WHERE EVENT_ID=? AND PRINCIPAL_REF_ID=? AND RANK_NO=1 AND CARGO=?";
			pstmt = con.prepareStatement(getBestOfferQry);
			pstmt.setString(1, eventId);
			pstmt.setString(2, principal_ref_id);
			pstmt.setString(3, cargo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				Y = rs.getString(1);
				tenderOfferRefId = rs.getString(2);
				M = rs.getString(3);
				C = rs.getString(4);
				C1_RAW = rs.getString(5);
				DP = rs.getString(6);

				if ("b".equals(mfilled)) {
					bestOffer += "Slope (%)-" + df2.format(Double.parseDouble(M)) + "<br/>";
				}
				if ("b".equals(cfilled)) {
					bestOffer += "Price/Constant (" + prodUnitName + ")-" + df2.format(Double.parseDouble(C)) + "<br/>";
				}
				if ("b".equals(c1filled)) {
					bestOffer += "Price/Constant (INR/" + prodUnitName.split("/")[1] + ")-"	+ df2.format(Double.parseDouble(C1_RAW));
				}

			}
			rs.close();
			pstmt.close();

			// GET OFFER FROM RA
			String minlp = "0";
			getBestOfferQry = "SELECT MIN(LP) FROM EPROC.IOCL_LNG_L1_BID WHERE  EVENT_ID=? AND CARGO=? GROUP BY CARGO";
			pstmt = con.prepareStatement(getBestOfferQry);
			pstmt.setString(1, eventId);
			pstmt.setString(2, cargo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				minlp = rs.getString(1);
			}
			rs.close();
			pstmt.close();

			String ra_rank_ref_id = "0";
			String RA_Y = "";
			getBestOfferQry = "SELECT RANK_REF_ID,Y,M_C_C1,BID_AMOUNT,DP FROM EPROC.IOCL_LNG_RA_LAST_BID WHERE EVENT_ID=? AND CARGO=? AND LP="+minlp;
			pstmt = con.prepareStatement(getBestOfferQry);
			pstmt.setString(1, eventId);
			pstmt.setString(2, cargo);
			//pstmt.setString(3, minlp);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				RA_Y = rs.getString(2);
				ra_rank_ref_id = rs.getString(1);
				String M_C_C1 = rs.getString(3);
				if ("M".equalsIgnoreCase(M_C_C1)) {
					bestOffer = "Slope (%)-" + df2.format(Double.parseDouble(rs.getString(4))) + "<br/>";
				}
				if ("C".equalsIgnoreCase(M_C_C1)) {
					bestOffer = "Price/Constant (" + prodUnitName + ")-"+ df2.format(Double.parseDouble(rs.getString(4))) + "<br/>";
				}
				if ("C1".equalsIgnoreCase(M_C_C1)) {
					bestOffer = "Price/Constant (INR/" + prodUnitName.split("/")[1] + ")-"+ df2.format(Double.parseDouble(rs.getString(4)));
				}
				DP = rs.getString(5);
			}
			rs.close();
			pstmt.close();

			if ((!tenderOfferRefId.equals(ra_rank_ref_id) || tenderOfferRefId.equals(ra_rank_ref_id)) && !ra_rank_ref_id.equals("0")) {
				Y = RA_Y;
			}

			// GET OFFER FROM COUNTER
			String co_rank_ref_id = "0";
			String CA_Y = "";
			getBestOfferQry = "SELECT MIN(LP) FROM EPROC.IOCL_LNG_L1_BID WHERE  EVENT_ID=? AND CARGO=? GROUP BY CARGO";
			pstmt = con.prepareStatement(getBestOfferQry);
			pstmt.setString(1, eventId);
			pstmt.setString(2, cargo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				minlp = rs.getString(1);
			}
			rs.close();
			pstmt.close();

			getBestOfferQry = "SELECT RANK_REF_ID,Y,M_C_C1,BID_AMOUNT,DP FROM EPROC.IOCL_LNG_COUNTER_LAST_BID WHERE EVENT_ID=? AND CARGO=? AND LP="+minlp;
			pstmt = con.prepareStatement(getBestOfferQry);
			pstmt.setString(1, eventId);
			pstmt.setString(2, cargo);
			//pstmt.setString(3, minlp);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				CA_Y = rs.getString(2);
				co_rank_ref_id = rs.getString(1);
				String M_C_C1 = rs.getString(3);
				if ("M".equalsIgnoreCase(M_C_C1)) {
					bestOffer = "Slope (%)-" + df2.format(Double.parseDouble(rs.getString(4))) + "<br/>";
				}
				if ("C".equalsIgnoreCase(M_C_C1)) {
					bestOffer = "Price/Constant (" + prodUnitName + ")-" + df2.format(Double.parseDouble(rs.getString(4))) + "<br/>";
				}
				if ("C1".equalsIgnoreCase(M_C_C1)) {
					bestOffer = "Price/Constant (INR/" + prodUnitName.split("/")[1] + ")-"+ df2.format(Double.parseDouble(rs.getString(4)));
				}
				DP = rs.getString(5);
			}
			rs.close();
			pstmt.close();

			if ((!tenderOfferRefId.equals(co_rank_ref_id) || tenderOfferRefId.equals(co_rank_ref_id)) && !co_rank_ref_id.equals("0")) {
				Y = CA_Y;
			}

			DP = df2.format(Double.parseDouble(DP));
			offerMap.put("BEST_OFFER", bestOffer);
			offerMap.put("DP", DP);

		} catch (Exception ff) {
			System.out.println("Error getBestOffer " + ff);
		}
		return offerMap;
	}// function end


	public static String getRefineryWithLAP_T7(Connection con, String tenderId, String principal_ref_id, String stage) {
		String mailBody = "";
		PreparedStatement psTender = null;
		ResultSet rs = null;
		String indentDet = "";
		//DecimalFormat df3 = new DecimalFormat("#0.000");
		DecimalFormat df3 = new java.text.DecimalFormat("##########0.00");
		Map<Integer, String> monthMap = new LinkedHashMap<Integer, String>();
		monthMap.put(1, "JAN");
		monthMap.put(2, "FEB");
		monthMap.put(3, "MAR");
		monthMap.put(4, "APR");
		monthMap.put(5, "MAY");
		monthMap.put(6, "JUN");
		monthMap.put(7, "JUL");
		monthMap.put(8, "AUG");
		monthMap.put(9, "SEP");
		monthMap.put(10, "OCT");
		monthMap.put(11, "NOV");
		monthMap.put(12, "DEC");

		try {
			PreparedStatement psCargoDet = null;
			ResultSet rsCargo = null;

			Map<String, String> dispIdNameMap = getDisport(con, principal_ref_id);
			Map<String, String> unitMap = getUnit(con, principal_ref_id);
			Map<String, String> refinIdNameMap = getRefinery(con, principal_ref_id);
			HashMap<String, String> qtyUnitMap = new HashMap<String, String>();

			String qtyUnitQuery = "SELECT UNIT_ID, UNIT_NAME FROM EPROC.IOCL_LNG_UNIT_MASTER WHERE ACTIVATED='y' AND PRINCIPAL_REF_ID="	+ principal_ref_id;
			PreparedStatement pstQtyUnit = con.prepareStatement(qtyUnitQuery);
			ResultSet rstQtyUnit = pstQtyUnit.executeQuery();
			while (rstQtyUnit.next()) {
				qtyUnitMap.put(rstQtyUnit.getString(1), rstQtyUnit.getString(2));
			}
			pstQtyUnit.close();
			rstQtyUnit.close();

			String prodUnitName = "";
			String unitQry = "SELECT u.UNIT_NAME FROM IOCL_LNG_UNIT_MASTER u, IOCL_LNG_TENDER_PROPERTIES t WHERE t.EVENT_ID=? and t.UNIT_PRODUCT_ID=u.UNIT_ID AND t.PRINCIPAL_REF_ID=? and t.PRINCIPAL_REF_ID=u.PRINCIPAL_REF_ID";
			psTender = con.prepareStatement(unitQry);
			psTender.setString(1, tenderId);
			psTender.setString(2, principal_ref_id);
			rs = psTender.executeQuery();

			if (rs.next()) {
				prodUnitName = rs.getString(1);
			}
			rs.close();
			psTender.close();

			String prodConfId = "", prodName = "", cargoQtyFrom = "", cargoQtyTo = "", cargoUnitName = "", prodId = "";
			String getIndentDet = "SELECT I.PRODUCT_ID, P.PROD_NAME,I.CARGO_QTY_FROM,I.CARGO_QTY_TO, I.UNIT_CARGO_QTY_ID,P.REF_ID FROM IOCL_LNG_TENDER_PROPERTIES I, IOCL_LNG_PRODUCT P, IOCL_LNG_PRODUCT_CONFIG pc WHERE  I.EVENT_ID = ? AND I.PRODUCT_ID = pc.REF_ID AND pc.PRODUCT_ID=p.REF_ID AND pc.PRINCIPAL_REF_ID=p.PRINCIPAL_REF_ID AND I.PRINCIPAL_REF_ID=p.PRINCIPAL_REF_ID AND p.PRINCIPAL_REF_ID=? AND p.ACTIVATED='y' AND pc.ACTIVATED='y'";

			psTender = con.prepareStatement(getIndentDet);
			psTender.setString(1, tenderId);
			psTender.setString(2, principal_ref_id);
			rs = psTender.executeQuery();
			if (rs.next()) {
				prodConfId = rs.getString(1);
				prodName = rs.getString(2);
				cargoQtyFrom = rs.getString(3);
				cargoQtyTo = rs.getString(4);
				String cargoUnitId = rs.getString(5);
				cargoUnitName = getUnit(con, principal_ref_id).get(cargoUnitId);
				prodId = rs.getString(6);
			}
			rs.close();
			psTender.close();
			//PTS#18628 - Field of "Each cargo quantity" to be removed
			/*
			if (cargoQtyFrom != null || cargoQtyTo != null) {
				double dbl_cargoQtyFrom = Double.parseDouble(cargoQtyFrom);
				double dbl_cargoQtyTo = Double.parseDouble(cargoQtyTo);

				indentDet = "<table width='1377' border = '1'>" + "<tbody>";

				indentDet += "<tr><th width='100' style='text-align:left;background-color:#ffa64d'>Each Cargo Quantity</th><td width='300' style='text-align:left;'>"
						+ ((dbl_cargoQtyFrom == (long) dbl_cargoQtyFrom) ? (long) dbl_cargoQtyFrom + ""
								: dbl_cargoQtyFrom)
								+ " to "
								+ ((dbl_cargoQtyTo == (long) dbl_cargoQtyTo) ? (long) dbl_cargoQtyTo + "" : dbl_cargoQtyTo)
								+ " " + cargoUnitName + "</td></tr>";

				indentDet = indentDet + "</tbody>" + "</table><br/>";
			}
			*/
			Map<String, String> formulaMasterMap = getFormulaMasterMap(con, principal_ref_id, prodConfId, tenderId);
			Map<String, String> paramValueMasterMap = getParamValueMasterMap(tenderId, prodConfId, principal_ref_id,con);

			String deemedQueryResult = "";
			String deemed_quantity_query = "SELECT HEADER_QTY FROM EPROC.IOCL_LNG_PRODUCT_CONFIG WHERE ACTIVATED='y' AND PRINCIPAL_REF_ID="	+ principal_ref_id + " AND REF_ID=" + prodConfId;
			PreparedStatement deemedPreparedStatement = con.prepareStatement(deemed_quantity_query);
			ResultSet deemedResultSet = deemedPreparedStatement.executeQuery();
			if (deemedResultSet.next()) {
				deemedQueryResult = deemedResultSet.getString(1);
			}
			deemedPreparedStatement.close();
			deemedResultSet.close();

			indentDet = indentDet + "<table width='1377' border = '1'>" + "<tbody>"
					+ "<tr style='background-color:#ffa64d;'>"
					+ "<td width='77' style = 'text-align:center;'><p><strong>Row ID</strong></p></td>"
					+ "<td width='100' style = 'display:none;'><p><strong>Basis</strong></p></td>"
					+ "<td width='300' style = 'text-align:center;'><p><strong>Delivery Window</strong></p></td>"
					+ "<td width='70' style = 'text-align:center;'><p><strong>No. of Cargoes in each Window</strong></p></td>"
					+ "<td width='200' style = 'text-align:center;'><p><strong>Discharge Port</strong></p></td>"
					+ "<td width='300' style = 'text-align:center;'><p><strong>Refineries</strong></p></td>"
					+ "<td width='200' style = 'text-align:center;'><p><strong>Landed Price (" + prodUnitName
					+ ")</strong></p></td>" + "</tr>";

			String getItemDetails = "SELECT REF_ID, FINAL_CARGO_ID, CARGO_ID FROM EPROC.IOCL_LNG_INDENT_PORTITEM WHERE INDENT_ID = ? AND STATUS = 'a'";
			List<String> nonRepeatedItems = new ArrayList<String>();
			String getIndentsOfATender = "SELECT DISTINCT pr.REF_ID, pr.EVENT_ID, pr.ITEM_ID, pr.CARGO, pr.DEL_TERM_ID, pr.M, pr.C, pr.C1, pr.Y, pr.DP, pr.VENDOR_ID FROM IOCL_LNG_POST_BID_RANK pr,IOCL_LNG_INDENT_REFINERY r WHERE pr.EVENT_ID=? AND pr.RANK_NO = 1 AND pr.PRINCIPAL_REF_ID = ? AND pr.ITEM_ID=r.ITEM_ID AND r.STATUS='a' AND r.REFINERY_ID IN (SELECT REF_CUST_ID FROM EPROC.IOCL_LNG_REFCUST WHERE STATUS = 'r') UNION ALL SELECT DISTINCT pr.REF_ID, pr.EVENT_ID, pr.ITEM_ID, pr.CARGO, pr.DEL_TERM_ID, pr.M, pr.C, pr.C1, pr.Y, pr.DP, pr.VENDOR_ID FROM IOCL_LNG_POST_BID_RANK pr WHERE pr.EVENT_ID=? AND pr.RANK_NO = 1 AND ITEM_ID = -1 AND pr.EVENT_ID IN (SELECT DISTINCT pr.EVENT_ID FROM IOCL_LNG_POST_BID_RANK pr,IOCL_LNG_INDENT_REFINERY r WHERE pr.EVENT_ID="+ tenderId + " AND pr.RANK_NO = 1 AND pr.PRINCIPAL_REF_ID = " + principal_ref_id+ " AND pr.ITEM_ID=r.ITEM_ID AND r.STATUS='a' AND r.REFINERY_ID IN (SELECT REF_CUST_ID FROM EPROC.IOCL_LNG_REFCUST WHERE STATUS = 'r')) ORDER BY 4,1";

			PreparedStatement pstInd = con.prepareStatement(getIndentsOfATender);
			pstInd.setString(1, tenderId);
			pstInd.setString(2, principal_ref_id);
			pstInd.setString(3, tenderId);
			ResultSet rsInd = pstInd.executeQuery();
			int itmCnt = 0;List<String> itemlist = new ArrayList<String>();
			while (rsInd.next()) {
				
				String itemId = rsInd.getString(3);
				if(!itemlist.contains(itemId)) itemlist.add(itemId);
					else continue;
				if(!nonRepeatedItems.contains(itemId)){
				nonRepeatedItems.add(itemId);
				String finalCargoLoc = rsInd.getString(4);
				String disportShow = "";
				String qry = "";
				boolean showRow = false;
				boolean isRA = false;
				
				Map <String,String> Cargo_RA_Stage_Map=new HashMap<String,String>(); 
				Map <String,String> Cargo_Stage_Map=new HashMap<String,String>(); 
				
				if ("AFTER_RA".equals(stage)) {
					//qry = "SELECT REF_ID FROM IOCL_LNG_RA_LAST_BID WHERE EVENT_ID=? AND ITEM_ID = ?";
					qry = "SELECT REF_ID FROM EPROC.IOCL_LNG_POST_BID_RANK WHERE EVENT_ID=? AND RA_STAT = 'y'";
					psCargoDet = con.prepareStatement(qry);
					psCargoDet.setString(1, tenderId);
					//psCargoDet.setString(2, itemId);
					rsCargo = psCargoDet.executeQuery();
					if (rsCargo.next()) {
						isRA = true;
					} else {
						isRA = false;
					}
					rsCargo.close();psCargoDet.close();
					
					if(isRA){
						qry="SELECT ITEM_ID FROM IOCL_LNG_RA_LAST_BID WHERE EVENT_ID = ? AND ITEM_ID = ? AND PRINCIPAL_REF_ID = ?";
						psCargoDet = con.prepareStatement(qry);
						psCargoDet.setString(1,tenderId);
						psCargoDet.setString(2,itemId);
						psCargoDet.setString(3,principal_ref_id);
						rsCargo = psCargoDet.executeQuery();
						if(rsCargo.next()){
							Cargo_RA_Stage_Map.put(itemId,"r");
						}
						rsCargo.close();psCargoDet.close();
					}
					
				}
				boolean isCo = false;
				if ("COUNTER".equals(stage)) {
					//qry = "SELECT REF_ID FROM IOCL_LNG_COUNTER_LAST_BID WHERE EVENT_ID=? AND ITEM_ID = ?";
					qry = "SELECT REF_ID FROM EPROC.IOCL_LNG_POST_BID_RANK WHERE EVENT_ID=? AND CO_STAT = 'y'";
					psCargoDet = con.prepareStatement(qry);
					psCargoDet.setString(1, tenderId);
					//psCargoDet.setString(2, itemId);
					rsCargo = psCargoDet.executeQuery();
					if (rsCargo.next()) {
						isCo = true;
					} else {
						isCo = false;
					}
					rsCargo.close();psCargoDet.close();
					
					qry="SELECT CARGO FROM IOCL_LNG_COUNTER WHERE EVENT_ID = ? AND ITEM_ID = ? AND PRINCIPAL_REF_ID = ?";
					psCargoDet = con.prepareStatement(qry);
					psCargoDet.setString(1,tenderId);
					psCargoDet.setString(2,itemId);
					psCargoDet.setString(3,principal_ref_id);
					rsCargo = psCargoDet.executeQuery();
					if(rsCargo.next()){
						Cargo_Stage_Map.put(itemId,"c");
					}
					rsCargo.close();psCargoDet.close();
				}

				if ("BEFORE_RA".equals(stage))
					showRow = true;
				if ("AFTER_RA".equals(stage) && isRA)
					showRow = true;
				if ("COUNTER".equals(stage) && isCo)
					showRow = true;

				if (showRow) {
					int dispCnt = 0;
					int totCargoCount = 0;
					int totRefinCount = 0;
					if (!"-1".equals(itemId)) {
						String getDispDet = "SELECT DISPORT_IDS, DISPLAY_ORDER, DISPORT_QTY, ALT_DISPORT, DISP_DEL_BASIS, DISP_DEL_UOM, REF_ID FROM EPROC.IOCL_LNG_INDENT_DISPORT WHERE STATUS = 'a' AND ITEM_ID = ? ORDER BY DISPLAY_ORDER";
						psCargoDet = con.prepareStatement(getDispDet);

						psCargoDet.setString(1, itemId);
						rsCargo = psCargoDet.executeQuery();
						while (rsCargo.next()) {
							String dispId = rsCargo.getString(1);
							String dispName = dispIdNameMap.get(dispId);
							String dispOrder = rsCargo.getString(2);
							String dispQty = rsCargo.getString(3);
							String altDispId = rsCargo.getString(4);
							String altDispName = dispIdNameMap.get(altDispId);
							String dispDelQtyBasis = rsCargo.getString(5);
							String dispDelUOM = rsCargo.getString(6);

							if (dispCnt == 0)
								disportShow = dispOrder + ". " + dispName + " - " + dispQty + " "+ unitMap.get(dispDelUOM);
							else
								disportShow = disportShow + "<br/>" + dispOrder + ". " + dispName + " - " + dispQty	+ " " + unitMap.get(dispDelUOM);

							dispCnt++;
						}
						rsCargo.close();
						psCargoDet.close();

						// GET CARGO DETAILS
						totCargoCount = getCargoCountItemwise(itemId, con);
						totRefinCount = getRefCountItemwise(itemId, con);
						int totRowspan = 0, carSpan = 0, refSpan = 0;
						try {
							totRowspan = totCargoCount * totRefinCount;
							carSpan = totRefinCount;
							refSpan = totCargoCount;
						} catch (Exception ex) {

						}
					}

					Map<String, String> basisMap = new LinkedHashMap<String, String>();
					Map<String, String> delWinMap = new LinkedHashMap<String, String>();
					Map<String, String> noOfCarMap = new LinkedHashMap<String, String>();
					List<String> cargoes = new ArrayList<String>();
					String getCargoDet = "SELECT REF_ID, FIRM_OPT, WINDOW_BASIS, VARCHAR_FORMAT(DEL_FROM_DATE,'DD-MM-YYYY'), VARCHAR_FORMAT(DEL_TO_DATE,'DD-MM-YYYY'), DEV_WIN, VARCHAR_FORMAT(DAVIATION_FROM,'DD-MM-YYYY'), VARCHAR_FORMAT(DAVIATION_TO,'DD-MM-YYYY'), NO_OF_WIN_CARGOS FROM EPROC.IOCL_LNG_INDENT_CARGO_CONFIG WHERE STATUS = 'a' AND ITEM_ID = ?";

					if (!"-1".equals(itemId)) {
						psCargoDet = con.prepareStatement(getCargoDet);
						psCargoDet.setString(1, itemId);
						rsCargo = psCargoDet.executeQuery();

						while (rsCargo.next()) {
							String carFirmOpt = rsCargo.getString(2);
							String carWindowBasis = rsCargo.getString(3);
							String carDelFrom = rsCargo.getString(4);
							String carDelTo = rsCargo.getString(5);
							String carDev = rsCargo.getString(6);
							String carDevFrm = rsCargo.getString(7);
							String carDevTo = rsCargo.getString(8);
							String carNoOfWin = rsCargo.getString(9);
							String carRefId = rsCargo.getString(1);
							cargoes.add(carRefId);

							if (carWindowBasis.equals("month range")) {
								carDelFrom = carDelFrom.split("-")[1] + "-" + carDelFrom.split("-")[2];
								carDelTo = carDelTo.split("-")[1] + "-" + carDelTo.split("-")[2];
								if (carDev.equals("Yes")) {
									carDevFrm = carDevFrm.split("-")[1] + "-" + carDevFrm.split("-")[2];
									carDevTo = carDevTo.split("-")[1] + "-" + carDevTo.split("-")[2];
								}
							} else if (carWindowBasis.equals("fixed month")) {
								carDelFrom = carDelFrom.split("-")[1] + "-" + carDelFrom.split("-")[2];
								carDelTo = "";
								if (carDev.equals("Yes")) {
									carDevFrm = carDevFrm.split("-")[1] + "-" + carDevFrm.split("-")[2];
									carDevTo = carDevTo.split("-")[1] + "-" + carDevTo.split("-")[2];
								}
							} else if (carWindowBasis.equals("date range")) {
								if ("11-11-1111".equals(carDevFrm))
									carDevFrm = "";
								if ("11-11-1111".equals(carDevTo))
									carDevTo = "";
							} else if (carWindowBasis.equals("fixed date")) {
								if ("11-11-1111".equals(carDelTo))
									carDelTo = "";
								if ("11-11-1111".equals(carDevFrm))
									carDevFrm = "";
								if ("11-11-1111".equals(carDevTo))
									carDevTo = "";
							}

							String delWindow = "";

							if (carDev.equals("Yes")) {
								delWindow = "<span style='text-decoration:underline;'>Preferred</span><br/>";
							}

							if (carWindowBasis.equals("date range")) {
								delWindow += carDelFrom + " to " + carDelTo;
							} else if (carWindowBasis.equals("fixed date")) {
								delWindow += carDelFrom;
							}

							if ("month range".equals(carWindowBasis) || "fixed month".equals(carWindowBasis)) {
								String[] carDelFrom_arr = carDelFrom.split("-");
								String monthStr = monthMap.get(Integer.parseInt(carDelFrom_arr[0]));
								String carDelFrom_show = monthStr + "-" + carDelFrom_arr[1];
								if ("month range".equals(carWindowBasis)) {
									String[] carDelTo_arr = carDelTo.split("-");
									String monthStr2 = monthMap.get(Integer.parseInt(carDelTo_arr[0]));
									String carDelTo_show = monthStr2 + "-" + carDelTo_arr[1];
									delWindow += carDelFrom_show + " to " + carDelTo_show;
								} else {
									delWindow += carDelFrom_show;
								}
							}

							if (carDev.equals("Yes")) {
								if ("month range".equals(carWindowBasis) || "fixed month".equals(carWindowBasis)) {
									String[] carDevFrm_arr = carDevFrm.split("-");
									String monthStr = monthMap.get(Integer.parseInt(carDevFrm_arr[0]));
									String carDevFrm_show = monthStr + "-" + carDevFrm_arr[1];
									String[] carDevTo_arr = carDevTo.split("-");
									String monthStr2 = monthMap.get(Integer.parseInt(carDevTo_arr[0]));
									String carDevTo_show = monthStr2 + "-" + carDevTo_arr[1];
									delWindow = delWindow+ "<br/><span style='text-decoration:underline;'>Allowed</span><br/>"+ carDevFrm_show + " to " + carDevTo_show;
								} else {
									delWindow = delWindow+ "<br/><span style='text-decoration:underline;'>Allowed</span><br/>"+ carDevFrm + " to " + carDevTo;
								}
							}

							basisMap.put(carRefId, carFirmOpt);
							delWinMap.put(carRefId, delWindow);
							noOfCarMap.put(carRefId, carNoOfWin);
						}
						rsCargo.close();
						psCargoDet.close();
					} else
						cargoes.add("-1");

					indentDet = indentDet + "<tr>" + "<td width='77' rowspan = " + totCargoCount
							+ " style = 'text-align:center;'><p>" + finalCargoLoc + "</p></td>";

					for (int i = 0; i < cargoes.size(); i++) {
						String carId = cargoes.get(i);
						if (i > 0)
							indentDet = indentDet + "<tr>";

						if (!"-1".equals(itemId)) {
							indentDet = indentDet + "<td width='100' style = 'display:none'><p>" + basisMap.get(carId)
									+ "</p></td>" + "<td width='300' style = 'text-align:center;'><p>"
									+ delWinMap.get(carId) + "</p></td>"
									+ "<td width='70' style = 'text-align:center;'><p>" + noOfCarMap.get(carId)
									+ "</p></td>";
						} else {
							indentDet = indentDet
									+ "<td width='470' colspan='3' style = 'text-align:center;'><p>This Row ID is a combination of all the Row IDs</p></td>";
						}

						if (i == 0) {
							if (!"-1".equals(itemId)) {
								indentDet = indentDet + "<td width='200' rowspan = " + totCargoCount
										+ " style = 'text-align:center;'><p>" + disportShow + "</p></td>";
							}

							// GET REFINERY DETAILS
							List<String> refRowList = new ArrayList<String>();
							Map<String, String> refinShMap = new LinkedHashMap<String, String>();
							Map<String, String> refinLapMap = new LinkedHashMap<String, String>();
							String refineryShow = "";
							String refShowTab = "<table style = 'border:none'>";
							String tmprefineryid = "", tmprefineryorder = "", tmprefineryqty = "", tmprefineryuom = "";
							List<String> refIdsHere = new ArrayList<String>();

							String getRefineryDet = "SELECT A.REFINERY_ID, A.DISPLAY_ORDER, A.REF_ID, A.REFINERY_QUANTITY, A.REF_DEL_UOM FROM EPROC.IOCL_LNG_INDENT_REFINERY A WHERE A.ITEM_ID = "+ itemId+ " AND A.STATUS = 'a' AND A.REFINERY_ID IN (SELECT REF_CUST_ID FROM EPROC.IOCL_LNG_REFCUST WHERE STATUS = 'r') ORDER BY CASE WHEN A.DISPLAY_ORDER = '' THEN 1 ELSE 0 END, A.DISPLAY_ORDER";

							if (itemId.equals("-1")) {
								getRefineryDet = "SELECT A.REFINERY_ID, A.DISPLAY_ORDER, A.REF_ID, A.REFINERY_QUANTITY, A.REF_DEL_UOM FROM EPROC.IOCL_LNG_INDENT_REFINERY A WHERE A.STATUS = 'a' AND A.INDENT_ID IN (SELECT DISTINCT INDENT_ID FROM EPROC.IOCL_LNG_INDENT_MASTER WHERE EVENT_ID = "	+ tenderId+ ") AND A.REFINERY_ID IN (SELECT REF_CUST_ID FROM EPROC.IOCL_LNG_REFCUST WHERE STATUS = 'r') ORDER BY CASE WHEN A.DISPLAY_ORDER = '' THEN 1 ELSE 0 END, A.DISPLAY_ORDER";
							}

							psCargoDet = con.prepareStatement(getRefineryDet);
							rsCargo = psCargoDet.executeQuery();
							int refCnt = 0;
							while (rsCargo.next()) {
								String refineryId = rsCargo.getString(1);
								String refineryName = refinIdNameMap.get(refineryId);
								String disorder = rsCargo.getString(2);
								String refineryRefId = rsCargo.getString(3);
								String refineryQty = rsCargo.getString(4);
								String refineryUom = rsCargo.getString(5);
								refIdsHere.add(refineryId);

								if (refCnt == 0)
									refineryShow = disorder.trim() + ". " + refineryName + "-" + refineryQty + " "+ unitMap.get(refineryUom);
								else
									refineryShow = refineryShow + "<br/>" + disorder + ". " + refineryName + "-"+ refineryQty + " " + unitMap.get(refineryUom);

								if ("NIL".equals(refineryQty)) {
									if (refCnt == 0)
										refineryShow = disorder.trim() + ". " + refineryName;
									else
										refineryShow = refineryShow + "<br/>" + disorder + ". " + refineryName;
								}

								if (refineryQty != null && "NIL".equals(refineryQty.trim())) {
									refinShMap.put(refineryId, disorder.trim() + "&nbsp;" + refineryName);
								} else {
									refinShMap.put(refineryId, disorder.trim() + "&nbsp;" + refineryName + "&nbsp;"	+ refineryQty + "&nbsp;" + unitMap.get(refineryUom) + "&nbsp;");
								}

								if (refCnt == 0)
									tmprefineryid = refineryRefId + "~" + refineryId;
								else
									tmprefineryid = tmprefineryid + "#" + refineryRefId + "~" + refineryId;

								if (refCnt == 0)
									tmprefineryorder = disorder;
								else
									tmprefineryorder = tmprefineryorder + "#" + disorder + " ";

								if (refCnt == 0)
									tmprefineryqty = refineryQty;
								else
									tmprefineryqty = tmprefineryqty + "#" + refineryQty;

								tmprefineryuom = refineryUom;

								refCnt++;

								Map<String, String> GnRMapRefinery = getGnR(con, principal_ref_id, tenderId, itemId,refineryId);
								if ("BEFORE_RA".equalsIgnoreCase(stage)) {
									Map<String, String> paramValMasterAll = new LinkedHashMap<String, String>();
									Map<String, String> vendorSpecificParamMapBeforeRA = getVendorSpecificParamMap(con,	principal_ref_id, tenderId, itemId, "BEFORE_RA");

									String F_VALUE = getFvalueCargoWise(tenderId, principal_ref_id, con, finalCargoLoc);
									String ER_VALUE = getERvalueCargoWise(tenderId, principal_ref_id, con, itemId);
									vendorSpecificParamMapBeforeRA.put("X", F_VALUE);
									vendorSpecificParamMapBeforeRA.put("ER", ER_VALUE);
									String C1_RAW = vendorSpecificParamMapBeforeRA.get("C1_RAW");
									double calculated_C1_VAL = 0.00;
									try {
										calculated_C1_VAL = Double.parseDouble(C1_RAW) / Double.parseDouble(ER_VALUE);
									} catch (Exception ex) {
									}

									vendorSpecificParamMapBeforeRA.put("C1", calculated_C1_VAL + "");
									List<String> l1vendorNdlterm = getL1VendorNdlterm(con, principal_ref_id, tenderId,	itemId, "BEFORE_RA");
									String l1_vendor = "", delveryTerm = "";
									if (l1vendorNdlterm.size() > 0) {
										l1_vendor = l1vendorNdlterm.get(0);
										delveryTerm = l1vendorNdlterm.get(1);
									}
									paramValMasterAll.putAll(paramValueMasterMap);
									paramValMasterAll.putAll(GnRMapRefinery);
									paramValMasterAll.putAll(vendorSpecificParamMapBeforeRA);
									
									String originalValueOfP4 = (String)paramValueMasterMap.get("P4");
									String originalValueOfP6 = (String)paramValueMasterMap.get("P6");
									String compnent_id = "0";
									getBidderCountryOfOriginMailUtils(l1_vendor,itemId,compnent_id,tenderId,con,paramValMasterAll,originalValueOfP4,originalValueOfP6);
									
									double cdfval = getEvaluatedValueForSpecFormula(con, principal_ref_id, "CDF", paramValMasterAll, formulaMasterMap);
									paramValMasterAll.put("CDF",cdfval+"");
									
									String SQL0="SELECT DISTINCT ITEM_ID FROM IOCL_LNG_PRICE_BID WHERE EVENT_ID=? AND ITEM_ID >0";
									PreparedStatement stmt0=con.prepareStatement(SQL0);
									stmt0.setString(1,tenderId);
									ResultSet rs0=stmt0.executeQuery();
									List<String> Itemlist = new ArrayList<String>();
									while(rs0.next()){
										String item_found=rs0.getString(1);
										Itemlist.add(item_found);
									}
									rs0.close();
									stmt0.close();
									
									if(Integer.parseInt(itemId.trim())==-1){
										double CDFAVG=0;
										Map<String,String> tempParamvaluemap = new LinkedHashMap<String,String>();
										tempParamvaluemap.putAll(paramValMasterAll);
										int countcomborow=Itemlist.size();
										//auditTrailMsg = auditTrailMsg + "\n countcomborow count "+countcomborow+" Combination row #tempParamvaluemap "+tempParamvaluemap;
										
										//SQL LOOP OF ITEMS
										for(String item_id_found :Itemlist){
											List<String> l1vendorNdlterm_local = getL1VendorNdlterm(con, principal_ref_id, tenderId, item_id_found, "BEFORE_RA");
											String l1_vendor_local = "";
											if(l1vendorNdlterm.size()>0){
												l1_vendor_local = l1vendorNdlterm.get(0);
											}
											getBidderCountryOfOriginMailUtils(l1_vendor_local,itemId,item_id_found,tenderId,con,tempParamvaluemap,originalValueOfP4,originalValueOfP6);
											double CDFval=getEvaluatedValueForSpecFormula(con, principal_ref_id, "CDF", tempParamvaluemap, formulaMasterMap);
											//out.println("Before RA CDFval"+CDFval+" FOR ITEM :"+item_id_found+"<br>");
											CDFAVG+=CDFval;
											//auditTrailMsg = auditTrailMsg + "\n item_id_found  "+item_id_found+" Combination row #tempParamvaluemap "+tempParamvaluemap+" CDF="+CDFval;			

										}//loop ends
										CDFAVG=(CDFAVG/countcomborow);
										//out.println("Before RA CDFAVG"+CDFAVG+"<br>");
										paramValMasterAll.put("CDF",CDFAVG+"");
										//auditTrailMsg = auditTrailMsg + "\n Combination row #paramValMasterAll "+paramValMasterAll;
									}	
									double CDval=getEvaluatedValueForSpecFormula(con, principal_ref_id, "CD", paramValMasterAll, formulaMasterMap);
									paramValMasterAll.put("CD",CDval+"");
									String DP_value = CalculateDPValue(tenderId, prodId, principal_ref_id, paramValMasterAll, l1_vendor, delveryTerm, con, true) + "";
									paramValMasterAll.put("DP", DP_value);
									double evalLAP_BeforeRA = getEvaluatedValueForSpecFormula(con, principal_ref_id, "LAP", paramValMasterAll, formulaMasterMap);
									refinLapMap.put(refineryId, evalLAP_BeforeRA + "");

								} else if ("AFTER_RA".equalsIgnoreCase(stage)) {
									Map<String, String> paramValMasterAll = new LinkedHashMap<String, String>();
									// CHECK IF BID PRESENT IN COUNTER
									boolean raExists = false;
									String chkRaBid = "SELECT M_C_C1,BID_AMOUNT FROM EPROC.IOCL_LNG_RA_LAST_BID WHERE EVENT_ID = ? AND ITEM_ID = ?";
									PreparedStatement psCntr = con.prepareStatement(chkRaBid);
									psCntr.setString(1, tenderId);
									psCntr.setString(2, itemId);
									ResultSet rsCntr = psCntr.executeQuery();
									if (rsCntr.next()) {
										raExists = true;
									}
									rsCntr.close();
									psCntr.close();

									if (raExists) {
										// ------CALCULATE LAP AFTER RA-------
										Map<String, String> vendorSpecificParamMapAfterRA = getVendorSpecificParamMap(
												con, principal_ref_id, tenderId, itemId, "AFTER_RA");
										paramValMasterAll.putAll(paramValueMasterMap);
										paramValMasterAll.putAll(GnRMapRefinery);
										paramValMasterAll.putAll(vendorSpecificParamMapAfterRA);
										
										List<String> l1vendorNdlterm = getL1VendorNdlterm(con, principal_ref_id, tenderId, itemId, "AFTER_RA");
										String l1_vendor = "",delveryTerm = "";
										if(l1vendorNdlterm.size()>0){
											l1_vendor = l1vendorNdlterm.get(0);
											delveryTerm = l1vendorNdlterm.get(1);
										}
										
										String originalValueOfP4 = (String)paramValueMasterMap.get("P4");
										String originalValueOfP6 = (String)paramValueMasterMap.get("P6");
										String compnent_id = "0";
										getBidderCountryOfOriginMailUtils(l1_vendor,itemId,compnent_id,tenderId,con,paramValMasterAll,originalValueOfP4,originalValueOfP6);
										
										double cdfval = getEvaluatedValueForSpecFormula(con, principal_ref_id, "CDF", paramValMasterAll, formulaMasterMap);
										paramValMasterAll.put("CDF",cdfval+"");
										
										String SQL0="SELECT DISTINCT ITEM_ID FROM IOCL_LNG_PRICE_BID WHERE EVENT_ID=? AND ITEM_ID >0";
										PreparedStatement stmt0=con.prepareStatement(SQL0);
										stmt0.setString(1,tenderId);
										ResultSet rs0=stmt0.executeQuery();
										List<String> Itemlist = new ArrayList<String>();
										while(rs0.next()){
											String item_found=rs0.getString(1);
											Itemlist.add(item_found);
										}
										rs0.close();
										stmt0.close();
										
										if(Integer.parseInt(itemId.trim())==-1){
											double CDFAVG=0;
											Map<String,String> tempParamvaluemap = new LinkedHashMap<String,String>();
											tempParamvaluemap.putAll(paramValMasterAll);
											int countcomborow=Itemlist.size();
											//auditTrailMsg = auditTrailMsg + "\n countcomborow count "+countcomborow+" Combination row #tempParamvaluemap "+tempParamvaluemap;
											
											//SQL LOOP OF ITEMS
												for(String item_id_found :Itemlist){
												List<String> l1vendorNdlterm_local = getL1VendorNdlterm(con, principal_ref_id, tenderId, item_id_found, "AFTER_RA");
												String l1_vendor_local = "";
												if(l1vendorNdlterm.size()>0){
												l1_vendor_local = l1vendorNdlterm.get(0);
												}
												getBidderCountryOfOriginMailUtils(l1_vendor_local,itemId,item_id_found,tenderId,con,tempParamvaluemap,originalValueOfP4,originalValueOfP6);
												double CDFval=getEvaluatedValueForSpecFormula(con, principal_ref_id, "CDF", tempParamvaluemap, formulaMasterMap);
												//out.println("Before RA CDFval"+CDFval+" FOR ITEM :"+item_id_found+"<br>");
												CDFAVG+=CDFval;
												//auditTrailMsg = auditTrailMsg + "\n item_id_found  "+item_id_found+" Combination row #tempParamvaluemap "+tempParamvaluemap+" CDF="+CDFval;			

											}//loop ends
											CDFAVG=(CDFAVG/countcomborow);
											//out.println("Before RA CDFAVG"+CDFAVG+"<br>");
											paramValMasterAll.put("CDF",CDFAVG+"");
											//auditTrailMsg = auditTrailMsg + "\n Combination row #paramValMasterAll "+paramValMasterAll;
										}	
										
										String F_VALUE = getFvalueCargoWise(tenderId, principal_ref_id, con, finalCargoLoc);
										String ER_VALUE = getERvalueCargoWise(tenderId, principal_ref_id, con, itemId);
										paramValMasterAll.put("X", F_VALUE);
										paramValMasterAll.put("ER", ER_VALUE);

										/*
										List<String> l1vendorNdlterm = getL1VendorNdlterm(con, principal_ref_id, tenderId, itemId, "AFTER_RA");
										String l1_vendor = "", delveryTerm = "";
										if (l1vendorNdlterm.size() > 0) {
											l1_vendor = l1vendorNdlterm.get(0);
											delveryTerm = l1vendorNdlterm.get(1);
										}
										*/
										double CDval=getEvaluatedValueForSpecFormula(con, principal_ref_id, "CD", paramValMasterAll, formulaMasterMap);
										paramValMasterAll.put("CD",CDval+"");
										String DP_value = CalculateDPValue(tenderId, prodId, principal_ref_id, paramValMasterAll, l1_vendor, delveryTerm, con, true) + "";
										paramValMasterAll.put("DP", DP_value);
										double evalLAP_AfterRA = getEvaluatedValueForSpecFormula(con, principal_ref_id,	"LAP", paramValMasterAll, formulaMasterMap);
										refinLapMap.put(refineryId, evalLAP_AfterRA + "");
									}else{
										//------CALCULATE LAP AFTER RA-------
										List<String> l1vendorNdlterm = getL1VendorNdlterm(con, principal_ref_id, tenderId, itemId, "BEFORE_RA");
										String l1_vendor = "",delveryTerm = "";
										if(l1vendorNdlterm.size()>0){
											l1_vendor = l1vendorNdlterm.get(0);
											delveryTerm = l1vendorNdlterm.get(1);
										}
										
										String originalValueOfP4 = (String)paramValueMasterMap.get("P4");
										String originalValueOfP6 = (String)paramValueMasterMap.get("P6");
										DecimalFormat df2 = new java.text.DecimalFormat("##########0.00");
										
										String prod_frmla = "";
										qry="SELECT FORMULA FROM EPROC.IOCL_LNG_FORMULA_MASTER WHERE REF_ID = (SELECT FORMULA_ID FROM EPROC.IOCL_LNG_PRODUCT_FORMULA_CONFIG WHERE REF_ID = (SELECT FORMULA_CONFIG_ID FROM EPROC.IOCL_LNG_TENDER_PROPERTIES WHERE PRINCIPAL_REF_ID = ? AND EVENT_ID = ?))";
										psCntr = con.prepareStatement(qry);
										psCntr.setString(1,principal_ref_id);
										psCntr.setString(2,tenderId);
										rs = psCntr.executeQuery();
										if(rs.next()){	
											prod_frmla = rs.getString(1);
										}
										rs.close();
										psCntr.close();
										
										Map<String,String> vendorSpecificParamMapAfterRA = getVendorSpecificParamMap(con, principal_ref_id, tenderId, itemId, "BEFORE_RA");
										paramValMasterAll.putAll(paramValueMasterMap);
										paramValMasterAll.putAll(vendorSpecificParamMapAfterRA);
										paramValMasterAll.putAll(GnRMapRefinery);
										
										String compnent_id = "0";
										getBidderCountryOfOriginMailUtils(l1_vendor,itemId,compnent_id,tenderId,con,paramValMasterAll,originalValueOfP4,originalValueOfP6);
										
										double cdfval = getEvaluatedValueForSpecFormula(con, principal_ref_id, "CDF", paramValMasterAll, formulaMasterMap);
										paramValMasterAll.put("CDF",cdfval+"");
										
										String SQL0="SELECT DISTINCT ITEM_ID FROM IOCL_LNG_PRICE_BID WHERE EVENT_ID=? AND ITEM_ID >0";
										PreparedStatement stmt0=con.prepareStatement(SQL0);
										stmt0.setString(1,tenderId);
										ResultSet rs0=stmt0.executeQuery();
										List<String> Itemlist = new ArrayList<String>();
										while(rs0.next()){
											String item_found=rs0.getString(1);
											Itemlist.add(item_found);
										}
										rs0.close();
										stmt0.close();
										
										if(Integer.parseInt(itemId.trim())==-1){
											double CDFAVG=0;
											Map<String,String> tempParamvaluemap = new LinkedHashMap<String,String>();
											tempParamvaluemap.putAll(paramValMasterAll);
											int countcomborow=Itemlist.size();
											//auditTrailMsg = auditTrailMsg + "\n countcomborow count "+countcomborow+" Combination row #tempParamvaluemap "+tempParamvaluemap;
											
											//SQL LOOP OF ITEMS
												for(String item_id_found :Itemlist){
												List<String> l1vendorNdlterm_local = getL1VendorNdlterm(con, principal_ref_id, tenderId, item_id_found, "BEFORE_RA");
												String l1_vendor_local = "";
												if(l1vendorNdlterm.size()>0){
												l1_vendor_local = l1vendorNdlterm.get(0);
												}
												getBidderCountryOfOriginMailUtils(l1_vendor_local,itemId,item_id_found,tenderId,con,tempParamvaluemap,originalValueOfP4,originalValueOfP6);
												double CDFval=getEvaluatedValueForSpecFormula(con, principal_ref_id, "CDF", tempParamvaluemap, formulaMasterMap);
												//out.println("Before RA CDFval"+CDFval+" FOR ITEM :"+item_id_found+"<br>");
												CDFAVG+=CDFval;
												//auditTrailMsg = auditTrailMsg + "\n item_id_found  "+item_id_found+" Combination row #tempParamvaluemap "+tempParamvaluemap+" CDF="+CDFval;			

											}//loop ends
											CDFAVG=(CDFAVG/countcomborow);
											//out.println("Before RA CDFAVG"+CDFAVG+"<br>");
											paramValMasterAll.put("CDF",CDFAVG+"");
											//auditTrailMsg = auditTrailMsg + "\n Combination row #paramValMasterAll "+paramValMasterAll;
										}
										
										String F_VALUE =getFvalueCargoWise(tenderId, principal_ref_id, con, finalCargoLoc); 
										String ER_VALUE =getERvalueCargoWise(tenderId, principal_ref_id, con, itemId);
										paramValMasterAll.put("X",F_VALUE);
										paramValMasterAll.put("ER",ER_VALUE);

										/*
										List<String> l1vendorNdlterm = getL1VendorNdlterm(con, principal_ref_id, tenderId, itemId, "AFTER_RA");
										String l1_vendor = "",delveryTerm = "";
										if(l1vendorNdlterm.size()>0){
											l1_vendor = l1vendorNdlterm.get(0);
											delveryTerm = l1vendorNdlterm.get(1);
										}
										*/

										//String compnent_id = "0";
										//String traceMessage = getBidderCountryOfOrigin(l1_vendor,itemId,compnent_id,tenderId,con,paramValueMasterMap,originalValueOfP4,originalValueOfP6);

										
										double CDval=getEvaluatedValueForSpecFormula(con, principal_ref_id, "CD", paramValMasterAll, formulaMasterMap);
										paramValMasterAll.put("CD",CDval+"");
										String DP_value = CalculateDPValue(tenderId, prodId, principal_ref_id, paramValMasterAll, l1_vendor, delveryTerm, con, true)+"";  
										paramValMasterAll.put("DP",DP_value);
										if(prod_frmla.equalsIgnoreCase("c") || prod_frmla.equalsIgnoreCase("mx") || prod_frmla.indexOf("mx+") != -1 || prod_frmla.indexOf("+mx") != -1){
											paramValMasterAll.put("Y", df2.format(Double.parseDouble(paramValMasterAll.get("Y"))));
										}
										double cdval = getEvaluatedValueForSpecFormula(con, principal_ref_id, "CD", paramValMasterAll, formulaMasterMap);
										paramValMasterAll.put("CD",cdval+"");
										double evalLAP_AfterRA = getEvaluatedValueForSpecFormula(con, principal_ref_id, "LAP", paramValMasterAll, formulaMasterMap);
										refinLapMap.put(refineryId,evalLAP_AfterRA+"");
										
										System.out.println("After RA paramValMasterAll: "+paramValMasterAll+" LAP VALUE: "+evalLAP_AfterRA);
									
									}/*else{
										String getLoadedPrice = "SELECT LAP_PRC, TENDER_FLAG FROM IOCL_LNG_ANNEXURE5_LD_PRC WHERE PRINCIPAL_REF_ID = ? AND EVENT_ID = ? AND REFINERY_ID = ? AND ITEM_ID =? AND TENDER_FLAG = 't'";
										psCntr = con.prepareStatement(getLoadedPrice);
										psCntr.setString(1,principal_ref_id);
										psCntr.setString(2,tenderId);
										psCntr.setString(3,refineryId);
										psCntr.setString(4,itemId);
										rsCntr = psCntr.executeQuery();
										while(rsCntr.next()){
											refinLapMap.put(refineryId,  rsCntr.getString(1) + "");
										}
										rsCntr.close();psCntr.close();
									}*/
								} else if ("COUNTER".equalsIgnoreCase(stage)) {
									// CHECK IF BID PRESENT IN COUNTER
									boolean counterExists = false;
									String chkCntrBid = "SELECT M_C_C1,BID_AMOUNT FROM EPROC.IOCL_LNG_COUNTER_LAST_BID WHERE EVENT_ID = ? AND ITEM_ID = ?";
									PreparedStatement psCntr = con.prepareStatement(chkCntrBid);
									psCntr.setString(1, tenderId);
									psCntr.setString(2, itemId);
									ResultSet rsCntr = psCntr.executeQuery();
									if (rsCntr.next()) {
										counterExists = true;
									}
									rsCntr.close();
									psCntr.close();

									if (counterExists) {
										Map<String, String> paramValMasterAll = new LinkedHashMap<String, String>();
										// ------CALCULATE LAP AFTER COUNTER-------
										Map<String, String> vendorSpecificParamMapAfterCNTR = getVendorSpecificParamMap(
												con, principal_ref_id, tenderId, itemId, "COUNTER");
										paramValMasterAll.putAll(paramValueMasterMap);
										paramValMasterAll.putAll(vendorSpecificParamMapAfterCNTR);
										paramValMasterAll.putAll(GnRMapRefinery);
										String F_VALUE = getFvalueCargoWise(tenderId, principal_ref_id, con,
												finalCargoLoc);
										String ER_VALUE = getERvalueCargoWise(tenderId, principal_ref_id, con, itemId);
										paramValMasterAll.put("X", F_VALUE);
										paramValMasterAll.put("ER", ER_VALUE);

										List<String> l1vendorNdlterm = getL1VendorNdlterm(con, principal_ref_id, tenderId, itemId, "COUNTER");
										String l1_vendor = "", delveryTerm = "";
										if (l1vendorNdlterm.size() > 0) {
											l1_vendor = l1vendorNdlterm.get(0);
											delveryTerm = l1vendorNdlterm.get(1);
										}
										String DP_value = CalculateDPValue(tenderId, prodId, principal_ref_id,	paramValMasterAll, l1_vendor, delveryTerm, con, true) + "";
										paramValMasterAll.put("DP", DP_value);

										double evalLAP_AfterCNTR = getEvaluatedValueForSpecFormula(con,	principal_ref_id, "LAP", paramValMasterAll, formulaMasterMap);
										refinLapMap.put(refineryId, evalLAP_AfterCNTR + "");
									}
								}

							}
							rsCargo.close();
							psCargoDet.close();
							if (refCnt == 1) {
								refineryShow = refineryShow.split("\\. ")[1];
							} else {
								for (int k = 0; k < refRowList.size(); k++) {
									refShowTab = refShowTab + refRowList.get(k);
								}
								refShowTab = refShowTab + "</table>";
								refineryShow = refShowTab;
							}
							indentDet = indentDet + "<td width='300' rowspan = " + totCargoCount + ">";
							for (Map.Entry<String, String> entry : refinShMap.entrySet()) {
								String refinIdSh = entry.getKey();
								indentDet = indentDet + refinShMap.get(refinIdSh) + "<br/><br/>";
							}
							indentDet = indentDet + "</td>";

							indentDet = indentDet + "<td width='200' rowspan = " + totCargoCount + ">";
							for (Map.Entry<String, String> entry : refinShMap.entrySet()) {
								String refinIdSh = entry.getKey();
								if (refinLapMap.containsKey(refinIdSh)) {
									indentDet = indentDet + df3.format(Double.parseDouble(refinLapMap.get(refinIdSh)))	+ "<br/><br/>";
								} else
									indentDet = indentDet + "NA" + "<br/><br/>";
							}
							indentDet = indentDet + "</td>";
							indentDet = indentDet + "</tr>";
						}
					}
					itmCnt++;
				}
				}
			}
			rsInd.close();
			pstInd.close();
			indentDet = indentDet + "</tbody>" + "</table>";

			mailBody = indentDet;
		} catch (Exception ex) {
			System.out.print("getRefineryWithLAP_T7:-" + ex);
			try {
				System.out.print("getRefineryWithLAP_T7:-"+ex);
			} catch (Throwable t) {
			}
		}
		return mailBody;
	}

	public static String getCustomerTable_T8(Connection con, String tenderId, String principal_ref_id, String stage) {
		String mailBody = "";
		PreparedStatement psTender = null;
		ResultSet rs = null;
		String indentDet = "";
		DecimalFormat df3 = new DecimalFormat("#0.000");
		Map<Integer, String> monthMap = new LinkedHashMap<Integer, String>();
		monthMap.put(1, "JAN");
		monthMap.put(2, "FEB");
		monthMap.put(3, "MAR");
		monthMap.put(4, "APR");
		monthMap.put(5, "MAY");
		monthMap.put(6, "JUN");
		monthMap.put(7, "JUL");
		monthMap.put(8, "AUG");
		monthMap.put(9, "SEP");
		monthMap.put(10, "OCT");
		monthMap.put(11, "NOV");
		monthMap.put(12, "DEC");

		try {
			PreparedStatement psCargoDet = null;
			ResultSet rsCargo = null;

			Map<String, String> dispIdNameMap = getDisport(con, principal_ref_id);
			Map<String, String> unitMap = getUnit(con, principal_ref_id);
			Map<String, String> refinIdNameMap = getRefinery(con, principal_ref_id);
			HashMap<String, String> qtyUnitMap = new HashMap<String, String>();

			String qtyUnitQuery = "SELECT UNIT_ID, UNIT_NAME FROM EPROC.IOCL_LNG_UNIT_MASTER WHERE ACTIVATED='y' AND PRINCIPAL_REF_ID="+ principal_ref_id;
			PreparedStatement pstQtyUnit = con.prepareStatement(qtyUnitQuery);
			ResultSet rstQtyUnit = pstQtyUnit.executeQuery();
			while (rstQtyUnit.next()) {
				qtyUnitMap.put(rstQtyUnit.getString(1), rstQtyUnit.getString(2));
			}
			pstQtyUnit.close();
			rstQtyUnit.close();

			String prodConfId = "", prodName = "", cargoQtyFrom = "", cargoQtyTo = "", cargoUnitName = "", prodId = "";
			String getIndentDet = "SELECT I.PRODUCT_ID, P.PROD_NAME,I.CARGO_QTY_FROM,I.CARGO_QTY_TO, I.UNIT_CARGO_QTY_ID,P.REF_ID FROM IOCL_LNG_TENDER_PROPERTIES I, IOCL_LNG_PRODUCT P, IOCL_LNG_PRODUCT_CONFIG pc WHERE  I.EVENT_ID = ? AND I.PRODUCT_ID = pc.REF_ID AND pc.PRODUCT_ID=p.REF_ID AND pc.PRINCIPAL_REF_ID=p.PRINCIPAL_REF_ID AND I.PRINCIPAL_REF_ID=p.PRINCIPAL_REF_ID AND p.PRINCIPAL_REF_ID=? AND p.ACTIVATED='y' AND pc.ACTIVATED='y'";

			psTender = con.prepareStatement(getIndentDet);
			psTender.setString(1, tenderId);
			psTender.setString(2, principal_ref_id);
			rs = psTender.executeQuery();
			if (rs.next()) {
				prodConfId = rs.getString(1);
				prodName = rs.getString(2);
				cargoQtyFrom = rs.getString(3);
				cargoQtyTo = rs.getString(4);
				String cargoUnitId = rs.getString(5);
				cargoUnitName = getUnit(con, principal_ref_id).get(cargoUnitId);
				prodId = rs.getString(6);
			}
			rs.close();
			psTender.close();
		//PTS#18628 - Field of "Each cargo quantity" to be removed
		/*
			if (cargoQtyFrom != null || cargoQtyTo != null) {
				double dbl_cargoQtyFrom = Double.parseDouble(cargoQtyFrom);
				double dbl_cargoQtyTo = Double.parseDouble(cargoQtyTo);

				indentDet = "<table width='1377' border = '1' bgcolor='#fff7e6'>" + "<tbody>";

				indentDet += "<tr><th width='100' style='text-align:left;background-color:#ffa64d;'>Each Cargo Quantity</th><td width='300' style='text-align:left;'>"
						+ ((dbl_cargoQtyFrom == (long) dbl_cargoQtyFrom) ? (long) dbl_cargoQtyFrom + ""
								: dbl_cargoQtyFrom)
								+ " to "
								+ ((dbl_cargoQtyTo == (long) dbl_cargoQtyTo) ? (long) dbl_cargoQtyTo + "" : dbl_cargoQtyTo)
								+ " " + cargoUnitName + "</td></tr>";

				indentDet = indentDet + "</tbody>" + "</table><br/>";
			}
		*/
			// out.println("indentDet: "+indentDet);

			Map<String, String> formulaMasterMap = getFormulaMasterMap(con, principal_ref_id, prodConfId, tenderId);
			Map<String, String> paramValueMasterMap = getParamValueMasterMap(tenderId, prodConfId, principal_ref_id,con);

			String deemedQueryResult = "";
			// String deemed_quantity_query = "SELECT HEADER_QTY FROM
			// EPROC.IOCL_LNG_PRODUCT WHERE ACTIVATED='y' AND
			// PRINCIPAL_REF_ID="+principal_ref_id+" AND REF_ID="+prodConfId;
			String deemed_quantity_query = "SELECT HEADER_QTY FROM EPROC.IOCL_LNG_PRODUCT_CONFIG WHERE ACTIVATED='y' AND PRINCIPAL_REF_ID="	+ principal_ref_id + " AND REF_ID=" + prodConfId;
			PreparedStatement deemedPreparedStatement = con.prepareStatement(deemed_quantity_query);
			ResultSet deemedResultSet = deemedPreparedStatement.executeQuery();
			if (deemedResultSet.next()) {
				deemedQueryResult = deemedResultSet.getString(1);
			}
			deemedPreparedStatement.close();
			deemedResultSet.close();

			String prodUnitName = "";
			String unitQry = "SELECT u.UNIT_NAME FROM IOCL_LNG_UNIT_MASTER u, IOCL_LNG_TENDER_PROPERTIES t WHERE t.EVENT_ID=? and t.UNIT_PRODUCT_ID=u.UNIT_ID AND t.PRINCIPAL_REF_ID=? and t.PRINCIPAL_REF_ID=u.PRINCIPAL_REF_ID";
			psTender = con.prepareStatement(unitQry);
			psTender.setString(1, tenderId);
			psTender.setString(2, principal_ref_id);
			rs = psTender.executeQuery();

			if (rs.next()) {
				prodUnitName = rs.getString(1);
			}
			rs.close();
			psTender.close();

			indentDet = indentDet + "<table width='1377' border = '1'>" + "<tbody>"
					+ "<tr style='background-color:#ffa64d;'>"
					+ "<td width='77' style = 'text-align:center;'><p><strong>Row ID</strong></p></td>"
					+ "<td width='100' style = 'display:none;'><p><strong>Basis</strong></p></td>"
					+ "<td width='300' style = 'text-align:center;'><p><strong>Delivery Window</strong></p></td>"
					+ "<td width='70' style = 'text-align:center;'><p><strong>No. of Cargoes in each Window</strong></p></td>"
					+ "<td width='200' style = 'text-align:center;display:none;'><p><strong>Discharge Port</strong></p></td>"
					+ "<td width='300' style = 'text-align:center;'><p><strong>Customers</strong></p></td>"
					+ "<td width='200' style = 'text-align:center;'><p><strong>Best Offer in Tender</strong></p></td>"
					+ "<td width='200' style = 'text-align:center;'><p><strong>Delivered Price (" + prodUnitName
					+ ")</strong></p></td>" + "</tr>";

			String getItemDetails = "SELECT REF_ID, FINAL_CARGO_ID, CARGO_ID FROM EPROC.IOCL_LNG_INDENT_PORTITEM WHERE INDENT_ID = ? AND STATUS = 'a'";
			List<String> nonRepeatedItems = new ArrayList<String>();
			// String getIndentsOfATender = "SELECT DISTINCT B.REF_ID,
			// B.FINAL_CARGO_ID, B.INDENT_ID FROM IOCL_LNG_TENDER_TABLE A,
			// IOCL_LNG_INDENT_PORTITEM B WHERE A.INDENT_ID = B.INDENT_ID AND
			// A.EVENT_ID=? ORDER BY B.FINAL_CARGO_ID";

			String getIndentsOfATender = "SELECT DISTINCT pr.REF_ID, pr.EVENT_ID, pr.ITEM_ID, pr.CARGO, pr.DEL_TERM_ID, pr.M, pr.C, pr.C1, pr.Y, pr.DP, pr.VENDOR_ID FROM IOCL_LNG_POST_BID_RANK pr,IOCL_LNG_INDENT_REFINERY r WHERE pr.EVENT_ID=? AND pr.RANK_NO = 1 AND pr.PRINCIPAL_REF_ID = ? AND pr.ITEM_ID=r.ITEM_ID AND r.STATUS='a' AND r.REFINERY_ID IN (SELECT REF_CUST_ID FROM EPROC.IOCL_LNG_REFCUST WHERE STATUS = 'c') UNION ALL SELECT DISTINCT pr.REF_ID, pr.EVENT_ID, pr.ITEM_ID, pr.CARGO, pr.DEL_TERM_ID, pr.M, pr.C, pr.C1, pr.Y, pr.DP, pr.VENDOR_ID FROM IOCL_LNG_POST_BID_RANK pr WHERE pr.EVENT_ID=? AND pr.RANK_NO = 1 AND ITEM_ID = -1 ORDER BY 4,1";

			PreparedStatement pstInd = con.prepareStatement(getIndentsOfATender);
			pstInd.setString(1, tenderId);
			pstInd.setString(2, principal_ref_id);
			pstInd.setString(3, tenderId);
			ResultSet rsInd = pstInd.executeQuery();
			int itmCnt = 0;
			while (rsInd.next()) {

				String itemId = rsInd.getString(3);
				if(!nonRepeatedItems.contains(itemId)){
				nonRepeatedItems.add(itemId);
				String finalCargoLoc = rsInd.getString(4);
				String disportShow = "";
				String qry = "";
				boolean showRow = false;
				boolean isRA = false;
				if ("AFTER_RA".equals(stage)) {
					//qry = "SELECT REF_ID FROM IOCL_LNG_RA_LAST_BID WHERE EVENT_ID=? AND ITEM_ID = ?";
					qry = "SELECT REF_ID FROM EPROC.IOCL_LNG_POST_BID_RANK WHERE EVENT_ID=? AND RA_STAT = 'y'";
					psCargoDet = con.prepareStatement(qry);
					psCargoDet.setString(1, tenderId);
					//psCargoDet.setString(2, itemId);
					rsCargo = psCargoDet.executeQuery();
					if (rsCargo.next()) {
						isRA = true;
					} else {
						isRA = false;
					}
					rsCargo.close();
					psCargoDet.close();
				}
				boolean isCo = false;
				if ("COUNTER".equals(stage)) {
					//qry = "SELECT REF_ID FROM IOCL_LNG_COUNTER_LAST_BID WHERE EVENT_ID=? AND ITEM_ID = ?";
					qry = "SELECT REF_ID FROM EPROC.IOCL_LNG_POST_BID_RANK WHERE EVENT_ID=? AND CO_STAT = 'y'";
					psCargoDet = con.prepareStatement(qry);
					psCargoDet.setString(1, tenderId);
					//psCargoDet.setString(2, itemId);
					rsCargo = psCargoDet.executeQuery();
					if (rsCargo.next()) {
						isCo = true;
					} else {
						isCo = false;
					}
					rsCargo.close();
					psCargoDet.close();
				}

				if ("BEFORE_RA".equals(stage))
					showRow = true;
				if ("AFTER_RA".equals(stage) && isRA)
					showRow = true;
				if ("COUNTER".equals(stage) && isCo)
					showRow = true;

				Map<String, String> besrtOfferMap = getBestOffer(tenderId, finalCargoLoc, principal_ref_id, con);
				String bestOffer = besrtOfferMap.get("BEST_OFFER");
				String DPvalue = besrtOfferMap.get("DP");

				int dispCnt = 0;
				int totCargoCount = 0;
				int totRefinCount = 0;

				if (showRow) {
					if (!"-1".equals(itemId)) {
						String getDispDet = "SELECT DISPORT_IDS, DISPLAY_ORDER, DISPORT_QTY, ALT_DISPORT, DISP_DEL_BASIS, DISP_DEL_UOM, REF_ID FROM EPROC.IOCL_LNG_INDENT_DISPORT WHERE STATUS = 'a' AND ITEM_ID = ? ORDER BY DISPLAY_ORDER";
						psCargoDet = con.prepareStatement(getDispDet);

						psCargoDet.setString(1, itemId);
						rsCargo = psCargoDet.executeQuery();
						while (rsCargo.next()) {
							String dispId = rsCargo.getString(1);
							String dispName = dispIdNameMap.get(dispId);
							String dispOrder = rsCargo.getString(2);
							String dispQty = rsCargo.getString(3);
							String altDispId = rsCargo.getString(4);
							String altDispName = dispIdNameMap.get(altDispId);
							String dispDelQtyBasis = rsCargo.getString(5);
							String dispDelUOM = rsCargo.getString(6);

							if (dispCnt == 0)
								disportShow = dispOrder + ". " + dispName + " - " + dispQty + " "+ unitMap.get(dispDelUOM);
							else
								disportShow = disportShow + "<br/>" + dispOrder + ". " + dispName + " - " + dispQty	+ " " + unitMap.get(dispDelUOM);

							dispCnt++;
						}
						rsCargo.close();
						psCargoDet.close();

						// GET CARGO DETAILS
						totCargoCount = getCargoCountItemwise(itemId, con);
						totRefinCount = getRefCountItemwise(itemId, con);

						int totRowspan = 0, carSpan = 0, refSpan = 0;
						try {
							totRowspan = totCargoCount * totRefinCount;
							carSpan = totRefinCount;
							refSpan = totCargoCount;
						} catch (Exception ex) {

						}
					}

					Map<String, String> basisMap = new LinkedHashMap<String, String>();
					Map<String, String> delWinMap = new LinkedHashMap<String, String>();
					Map<String, String> noOfCarMap = new LinkedHashMap<String, String>();
					List<String> cargoes = new ArrayList<String>();
					String getCargoDet = "SELECT REF_ID, FIRM_OPT, WINDOW_BASIS, VARCHAR_FORMAT(DEL_FROM_DATE,'DD-MM-YYYY'), VARCHAR_FORMAT(DEL_TO_DATE,'DD-MM-YYYY'), DEV_WIN, VARCHAR_FORMAT(DAVIATION_FROM,'DD-MM-YYYY'), VARCHAR_FORMAT(DAVIATION_TO,'DD-MM-YYYY'), NO_OF_WIN_CARGOS FROM EPROC.IOCL_LNG_INDENT_CARGO_CONFIG WHERE STATUS = 'a' AND ITEM_ID = ?";

					if (!"-1".equals(itemId)) {
						psCargoDet = con.prepareStatement(getCargoDet);
						psCargoDet.setString(1, itemId);
						rsCargo = psCargoDet.executeQuery();

						while (rsCargo.next()) {
							String carFirmOpt = rsCargo.getString(2);
							String carWindowBasis = rsCargo.getString(3);
							String carDelFrom = rsCargo.getString(4);
							String carDelTo = rsCargo.getString(5);
							String carDev = rsCargo.getString(6);
							String carDevFrm = rsCargo.getString(7);
							String carDevTo = rsCargo.getString(8);
							String carNoOfWin = rsCargo.getString(9);
							String carRefId = rsCargo.getString(1);
							cargoes.add(carRefId);

							if (carWindowBasis.equals("month range")) {
								carDelFrom = carDelFrom.split("-")[1] + "-" + carDelFrom.split("-")[2];
								carDelTo = carDelTo.split("-")[1] + "-" + carDelTo.split("-")[2];
								if (carDev.equals("Yes")) {
									carDevFrm = carDevFrm.split("-")[1] + "-" + carDevFrm.split("-")[2];
									carDevTo = carDevTo.split("-")[1] + "-" + carDevTo.split("-")[2];
								}
							} else if (carWindowBasis.equals("fixed month")) {
								carDelFrom = carDelFrom.split("-")[1] + "-" + carDelFrom.split("-")[2];
								carDelTo = "";
								if (carDev.equals("Yes")) {
									carDevFrm = carDevFrm.split("-")[1] + "-" + carDevFrm.split("-")[2];
									carDevTo = carDevTo.split("-")[1] + "-" + carDevTo.split("-")[2];
								}
							} else if (carWindowBasis.equals("date range")) {
								if ("11-11-1111".equals(carDevFrm))
									carDevFrm = "";
								if ("11-11-1111".equals(carDevTo))
									carDevTo = "";
							} else if (carWindowBasis.equals("fixed date")) {
								if ("11-11-1111".equals(carDelTo))
									carDelTo = "";
								if ("11-11-1111".equals(carDevFrm))
									carDevFrm = "";
								if ("11-11-1111".equals(carDevTo))
									carDevTo = "";
							}

							String delWindow = "";

							if (carDev.equals("Yes")) {
								delWindow = "<span style='text-decoration:underline;'>Preferred</span><br/>";
							}

							if (carWindowBasis.equals("date range")) {
								delWindow += carDelFrom + " to " + carDelTo;
							} else if (carWindowBasis.equals("fixed date")) {
								delWindow += carDelFrom;
							}

							if ("month range".equals(carWindowBasis) || "fixed month".equals(carWindowBasis)) {
								String[] carDelFrom_arr = carDelFrom.split("-");
								String monthStr = monthMap.get(Integer.parseInt(carDelFrom_arr[0]));
								String carDelFrom_show = monthStr + "-" + carDelFrom_arr[1];
								if ("month range".equals(carWindowBasis)) {
									String[] carDelTo_arr = carDelTo.split("-");
									String monthStr2 = monthMap.get(Integer.parseInt(carDelTo_arr[0]));
									String carDelTo_show = monthStr2 + "-" + carDelTo_arr[1];
									delWindow += carDelFrom_show + " to " + carDelTo_show;
								} else {
									delWindow += carDelFrom_show;
								}
							}

							if (carDev.equals("Yes")) {
								if ("month range".equals(carWindowBasis) || "fixed month".equals(carWindowBasis)) {
									String[] carDevFrm_arr = carDevFrm.split("-");
									String monthStr = monthMap.get(Integer.parseInt(carDevFrm_arr[0]));
									String carDevFrm_show = monthStr + "-" + carDevFrm_arr[1];
									String[] carDevTo_arr = carDevTo.split("-");
									String monthStr2 = monthMap.get(Integer.parseInt(carDevTo_arr[0]));
									String carDevTo_show = monthStr2 + "-" + carDevTo_arr[1];
									delWindow = delWindow
											+ "<br/><span style='text-decoration:underline;'>Allowed</span><br/>"
											+ carDevFrm_show + " to " + carDevTo_show;
								} else {
									delWindow = delWindow
											+ "<br/><span style='text-decoration:underline;'>Allowed</span><br/>"
											+ carDevFrm + " to " + carDevTo;
								}
							}

							basisMap.put(carRefId, carFirmOpt);
							delWinMap.put(carRefId, delWindow);
							noOfCarMap.put(carRefId, carNoOfWin);
						}
						rsCargo.close();
						psCargoDet.close();
					} else
						cargoes.add("-1");

					indentDet = indentDet + "<tr>" + "<td width='77' rowspan = " + totCargoCount
							+ " style = 'text-align:center;'><p>" + finalCargoLoc + "</p></td>";

					for (int i = 0; i < cargoes.size(); i++) {
						String carId = cargoes.get(i);
						if (i > 0)
							indentDet = indentDet + "<tr>";

						if (!"-1".equals(itemId)) {
							indentDet = indentDet + "<td width='100' style = 'display:none'><p>" + basisMap.get(carId)
									+ "</p></td>" + "<td width='300' style = 'text-align:center;'><p>"
									+ delWinMap.get(carId) + "</p></td>"
									+ "<td width='70' style = 'text-align:center;'><p>" + noOfCarMap.get(carId)
									+ "</p></td>";
						} else {
							indentDet = indentDet
									+ "<td width='470' colspan='2' style = 'text-align:left;'><p>This Row ID is a combination of all the Row IDs</p></td>";
						}

						if (i == 0) {
							if (!"-1".equals(itemId)) {
								indentDet = indentDet + "<td width='200' rowspan = " + totCargoCount
										+ " style = 'text-align:center;display:none;'><p>" + disportShow + "</p></td>";
							}

							// GET REFINERY DETAILS
							List<String> refRowList = new ArrayList<String>();
							Map<String, String> refinShMap = new LinkedHashMap<String, String>();
							Map<String, String> refinLapMap = new LinkedHashMap<String, String>();
							String refineryShow = "";
							String refShowTab = "<table style = 'border:none'>";
							String tmprefineryid = "", tmprefineryorder = "", tmprefineryqty = "", tmprefineryuom = "";
							List<String> refIdsHere = new ArrayList<String>();

							String getRefineryDet = "SELECT A.REFINERY_ID, A.DISPLAY_ORDER, A.REF_ID, A.REFINERY_QUANTITY, A.REF_DEL_UOM FROM EPROC.IOCL_LNG_INDENT_REFINERY A WHERE A.ITEM_ID = "+ itemId+ " AND A.STATUS = 'a' AND A.REFINERY_ID IN (SELECT REF_CUST_ID FROM EPROC.IOCL_LNG_REFCUST WHERE STATUS = 'c') ORDER BY CASE WHEN A.DISPLAY_ORDER = '' THEN 1 ELSE 0 END, A.DISPLAY_ORDER";

							if (itemId.equals("-1")) {
								getRefineryDet = "SELECT A.REFINERY_ID, A.DISPLAY_ORDER, A.REF_ID, A.REFINERY_QUANTITY, A.REF_DEL_UOM FROM EPROC.IOCL_LNG_INDENT_REFINERY A WHERE A.STATUS = 'a' AND A.INDENT_ID IN (SELECT DISTINCT INDENT_ID FROM EPROC.IOCL_LNG_INDENT_MASTER WHERE EVENT_ID = "	+ tenderId+ ") AND A.REFINERY_ID IN (SELECT REF_CUST_ID FROM EPROC.IOCL_LNG_REFCUST WHERE STATUS = 'c') ORDER BY CASE WHEN A.DISPLAY_ORDER = '' THEN 1 ELSE 0 END, A.DISPLAY_ORDER";
							}

							psCargoDet = con.prepareStatement(getRefineryDet);
							rsCargo = psCargoDet.executeQuery();
							int refCnt = 0;
							while (rsCargo.next()) {
								String refineryId = rsCargo.getString(1);
								String refineryName = refinIdNameMap.get(refineryId);
								String disorder = rsCargo.getString(2);
								String refineryRefId = rsCargo.getString(3);
								String refineryQty = rsCargo.getString(4);
								String refineryUom = rsCargo.getString(5);
								refIdsHere.add(refineryId);

								if (refCnt == 0)
									refineryShow = disorder.trim() + ". " + refineryName + "-" + refineryQty + " "+ unitMap.get(refineryUom);
								else
									refineryShow = refineryShow + "<br/>" + disorder + ". " + refineryName + "-"+ refineryQty + " " + unitMap.get(refineryUom);

								if ("NIL".equals(refineryQty)) {
									if (refCnt == 0)
										refineryShow = disorder.trim() + ". " + refineryName;
									else
										refineryShow = refineryShow + "<br/>" + disorder + ". " + refineryName;
								}

								if (refineryQty != null && "NIL".equals(refineryQty.trim())) {
									refinShMap.put(refineryId, disorder.trim() + "&nbsp;" + refineryName);
								} else {
									refinShMap.put(refineryId, disorder.trim() + "&nbsp;" + refineryName + "&nbsp;"	+ refineryQty + "&nbsp;" + unitMap.get(refineryUom) + "&nbsp;");
								}

								if (refCnt == 0)
									tmprefineryid = refineryRefId + "~" + refineryId;
								else
									tmprefineryid = tmprefineryid + "#" + refineryRefId + "~" + refineryId;

								if (refCnt == 0)
									tmprefineryorder = disorder;
								else
									tmprefineryorder = tmprefineryorder + "#" + disorder + " ";

								if (refCnt == 0)
									tmprefineryqty = refineryQty;
								else
									tmprefineryqty = tmprefineryqty + "#" + refineryQty;

								tmprefineryuom = refineryUom;

								refCnt++;

							}
							rsCargo.close();
							psCargoDet.close();
							if (refCnt == 1) {
								refineryShow = refineryShow.split("\\. ")[1];
							} else {
								for (int k = 0; k < refRowList.size(); k++) {
									refShowTab = refShowTab + refRowList.get(k);
								}
								refShowTab = refShowTab + "</table>";
								refineryShow = refShowTab;
							}

							indentDet = indentDet + "<td width='300' rowspan = " + totCargoCount + ">";
							for (Map.Entry<String, String> entry : refinShMap.entrySet()) {
								String refinIdSh = entry.getKey();
								indentDet = indentDet + refinShMap.get(refinIdSh) + "<br/><br/>";
							}
							indentDet = indentDet + "</td>";

							indentDet = indentDet + "<td width='300' rowspan = '" + totCargoCount + "'>" + bestOffer
									+ "</td>";
							indentDet = indentDet + "<td width='300' rowspan = '" + totCargoCount
									+ "' style = 'text-align:center;'>" + DPvalue + "</td>";

							indentDet = indentDet + "</tr>";

						}
					}
					itmCnt++;
				}
				}
			}
			rsInd.close();
			pstInd.close();
			indentDet = indentDet + "</tbody>" + "</table>";

			mailBody = indentDet;
		} catch (Exception ex) {
			System.out.print("getCustomerTable_T8:-" + ex);
			try {
				// out.print("getCustomerTable_T8:-"+ex);
			} catch (Throwable t) {
			}
		}
		return mailBody;
	}

	public static String getTenderTimelines_T4(Connection con, String tenderId, String principal_ref_id,boolean isTenderPublished){
		String mailBody = "";
		PreparedStatement psTender=null;
		ResultSet rs=null;

		try{
			String evtStartTime = "", evtCloseTime = "", evtPublishTime = "", auctionStTime = "", bidValidityTime = "", toeTime = "", publishTime = "";
			String getTenderMasterData = "SELECT EVENT_NO,VARCHAR_FORMAT(EVENT_ST_TIME,'DD-MM-YYYY HH24:MI'),VARCHAR_FORMAT(EVENT_CL_DATE,'DD-MM-YYYY HH24:MI'),VARCHAR_FORMAT(EVENT_CL_TIME,'DD-MM-YYYY HH24:MI'),VARCHAR_FORMAT(EVENT_VIEW_TIME,'DD-MM-YYYY HH24:MI'),VARCHAR_FORMAT(TOE_TIME,'DD-MM-YYYY HH24:MI'),NO_OF_TC_MEMBERS,VARCHAR_FORMAT(EMD_SUB_TIME,'DD-MM-YYYY HH24:MI'),CURRENCY,VARCHAR_FORMAT(PBMEET_ST_TIME,'DD-MM-YYYY HH24:MI') FROM EP_EVENT_MASTER WHERE REF_ID = ? AND PRINCIPAL_REF_ID = ?";
			psTender = con.prepareStatement(getTenderMasterData);
			psTender.setString(1,tenderId);
			psTender.setString(2,principal_ref_id);
			rs = psTender.executeQuery();
			if(rs.next()){
				evtStartTime = rs.getString(2);
				evtCloseTime = rs.getString(4);
				toeTime = rs.getString(6);
				bidValidityTime = rs.getString(8);
				auctionStTime = rs.getString(10);
				publishTime = rs.getString(5);
			}rs.close();psTender.close();

			String tenderTimeTable = ""+
					"<table width='610' border ='1'>"+
					"<tbody>";
			if(isTenderPublished){
				tenderTimeTable =tenderTimeTable +"<tr>"+
						"<td width='356' style='background-color:#ffa64d;'><p>Tender Publishing Date &amp; Time (in IST)</p></td>"+
						"<td width='255'><p>"+publishTime+" Hrs.</p></td>"+
						"</tr>";
			}
			tenderTimeTable =tenderTimeTable +"<tr>"+
					"<td width='356' style='background-color:#ffa64d;'><p>Bid Submission Start Date &amp; Time (in IST)</p></td>"+
					"<td width='255'><p>"+evtStartTime+" Hrs.</p></td>"+
					"</tr>"+
					"<tr>"+
					"<td width='356' style='background-color:#ffa64d;'><p>Bid Submission End Date &amp; Time (in IST)</p></td>"+
					"<td width='255'><p>"+evtCloseTime+" Hrs.</p></td>"+
					"</tr>"+
					"<tr>"+
					"<td width='356' style='background-color:#ffa64d;'><p>Bid Opening Date &amp; Time (in IST)</p></td>"+
					"<td width='255'><p>"+toeTime+" Hrs.</p></td>"+
					"</tr>"+
					"<tr>"+
					"<td width='356' style='background-color:#ffa64d;'><p>Reverse Auction Start Date &amp; Time (in IST)</p></td>"+
					"<td width='255'><p>"+auctionStTime+" Hrs.</p></td>"+
					"</tr>"+
					"<tr>"+
					"<td width='356' style='background-color:#ffa64d;'><p>Bid Validity Date &amp; Time (in IST)</p></td>"+
					"<td width='255'><p>"+bidValidityTime+" Hrs.</p></td>"+
					"</tr>"+
					"</tbody>"+
					"</table>";


			mailBody = tenderTimeTable;
		}catch(Exception ex){
			System.out.print("getTenderTimelines_T4:-"+ex);		
		}
		return mailBody;
	}

	public static boolean updateMailLogUserTender(List<String> userList, String principal, String tenderId,String indentIds, String content, String subject, String subHeader, Map<String,String> toMailsMap, Map<String,String> ccMailsMap) throws Exception{
		Connection localcon = null;
		boolean isInserted = false;
		PreparedStatement psBatch = null, psBatch1=null;
		ResultSet rsMail = null;
		String cc_mails = "";
		String to_mails = "";
		String mailId="";
		try{
			
			ConnectionMaker conn=new ConnectionMaker();
			//connect conn = new connect();
			//conn.setUserType("u");
			localcon = conn.getConnection("eproc");
			localcon.setAutoCommit(false);

			String insertMailLog = "SELECT MAIL_ID FROM NEW TABLE(INSERT INTO EPROC.IOCL_LNG_MAIL_BOX (EVENT_ID, PRINCIPAL_REF_ID, SUBJECT, CONTENT,INDENT_IDS, ENTRY_TIME) VALUES(?,?,?,?,?,CURRENT TIMESTAMP))";

			String insertMailLog_Recipient = "INSERT INTO EPROC.IOCL_LNG_MAIL_RECIPIENT (MAIL_ID, VENDOR_ID, USER_REF_ID, CC_USERS) VALUES(?,?,?,?)";


			psBatch = localcon.prepareStatement(insertMailLog);
			psBatch1 = localcon.prepareStatement(insertMailLog_Recipient);
			List<String> tomailsList = new ArrayList<String>(toMailsMap.values());
			List<String> ccmailsList = new ArrayList<String>(ccMailsMap.values());
			List<String> ccIdsList = new ArrayList<String>(ccMailsMap.keySet());
			String ccIds=StringUtils.join(ccIdsList, ",");
			boolean isMailInserted=false;
			userList.addAll(ccIdsList);
			for(int i=0;i<userList.size(); i++){
				String userRefId = userList.get(i);
				to_mails = "<b>To:</b> "+tomailsList.toString()+"<br/>";
				if(ccmailsList.size()>0){
					cc_mails = "<b>Cc:</b> "+ccmailsList.toString()+"<br/><br/>";
				}else{
					cc_mails = "<br/>";
				}
				String mailContent = to_mails+cc_mails+"<p style='text-align:center;font-weight:bold;'>Subject: "+subject+"</p><br/>"+content;
				mailContent = removeMultiSpace(mailContent);

				if(!isMailInserted){
					psBatch.setString(1,tenderId);
					psBatch.setString(2,principal);
					psBatch.setString(3,subHeader);
					psBatch.setString(4,mailContent);
					psBatch.setString(5,indentIds);
					ResultSet rsBatch=psBatch.executeQuery();
					if(rsBatch.next()){
						mailId=rsBatch.getString(1);
					}
					rsBatch.close();
					psBatch.close();
					isMailInserted=true;
				} 

				psBatch1.setString(1,mailId);
				psBatch1.setString(2,"-1");
				psBatch1.setString(3,userRefId);
				psBatch1.setString(4,ccIds);
				psBatch1.addBatch();
			}
			int res[]=psBatch1.executeBatch();
			System.out.println("Result count is::::"+res[0]);
			psBatch1.close();
			localcon.commit();
			isInserted = true;
			localcon.close();
		}catch(Exception ex){
			isInserted = false;
			throw ex;
		}
		return isInserted;
	}

	public static Map<String,String> getPriceAcceptorIdsByDept(Connection con, String principal_ref_id, String prodCat,int deptId){
		Map<String,String> idMailMap = new LinkedHashMap<String,String>();
		List<String> refIds = new ArrayList<String>();
		PreparedStatement psTender=null;
		ResultSet rs2=null;
		try{
			String getIND = "SELECT PMR.CATEGORY,PMR.ROLES,PMR.PM_USER_ID,PM.USER_REF_ID,PM.EMAIL FROM EPROC.EP_PM_USERS PM,IOCL_LNG_PM_USERS_ROLES PMR WHERE  PM.USER_REF_ID = PMR.PM_USER_ID AND PM.PRINCIPAL_REF_ID=? AND PM.ACTIVATE = 'y' AND PM.DSC_STATUS = 'y' AND (PMR.CATEGORY like '"+prodCat+"#%' OR PMR.CATEGORY like '%#"+prodCat+"' OR PMR.CATEGORY like '%#"+prodCat+"#%' OR PMR.CATEGORY = ?) AND PMR.DEPT="+deptId+" AND PMR.STATUS = 'a' AND PMR.PRINCIPAL_REF_ID = ?";
			psTender=con.prepareStatement(getIND);
			psTender.setString(1,principal_ref_id);
			psTender.setString(2,prodCat);
			psTender.setString(3,principal_ref_id);
			rs2=psTender.executeQuery();
			while(rs2.next()){
				String category_db[] = rs2.getString(1).split("#");
				String roles_db[] = rs2.getString(2).split("#");
				for(int i=0;i<category_db.length;i++){
					if(category_db[i].equals(prodCat)){
						if(roles_db[i].contains("PAC,") || roles_db[i].contains(",PAC,") || roles_db[i].contains(",PAC") || roles_db[i].equals("PAC")){
							String ref_id = rs2.getString(4);
							String mail_id = rs2.getString(5);
							refIds.add(ref_id);
							idMailMap.put(ref_id,mail_id);	
						}
					}
				}
			}
			rs2.close();
			psTender.close();
		}catch(Exception ex){
			System.out.print("getPriceAcceptorIdsByDept mails:-"+ex);
		}
		return idMailMap;
	}

	public static List<String> getL1VendorNdlterm(Connection con, String principal_ref_id, String event_id,
			String itemId, String stage) throws Exception {
		List<String> L1VendorNdlterm = new ArrayList<String>();
		try {
			String query = "SELECT VENDOR_ID, DEL_TERM_ID FROM EPROC.IOCL_LNG_POST_BID_RANK WHERE EVENT_ID = ? AND ITEM_ID = ? AND RANK_NO = '1' ORDER BY LP, DP, Y, M, C, C1, REF_ID";
			if ("BEFORE_RA".equals(stage)) {
				query = "SELECT VENDOR_ID, DEL_TERM_ID FROM EPROC.IOCL_LNG_POST_BID_RANK WHERE EVENT_ID = ? AND ITEM_ID = ? AND RANK_NO = '1' ORDER BY LP, DP, Y, M, C, C1, REF_ID";
			} else if ("AFTER_RA".equals(stage)) {
				query = "SELECT VENDOR_ID, DEL_TERM_ID FROM IOCL_LNG_RA_LAST_BID WHERE EVENT_ID=? AND ITEM_ID=? ORDER BY LP, DP, Y, BID_AMOUNT, REF_ID";
			} else if ("COUNTER".equals(stage)) {
				query = "SELECT VENDOR_ID, DEL_TERM_ID FROM IOCL_LNG_COUNTER_LAST_BID WHERE EVENT_ID=? AND ITEM_ID=? ORDER BY LP, DP, Y, BID_AMOUNT, REF_ID";
			}
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, event_id);
			pstmt.setString(2, itemId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String l1_vendorId = rs.getString(1);
				String dltermid = rs.getString(2);
				L1VendorNdlterm.add(l1_vendorId);
				L1VendorNdlterm.add(dltermid);
			}
			rs.close();
			pstmt.close();

		} catch (Exception ex) {
			System.out.println("Exception in getL1VendorNdlterm: " + ex);
		}
		return L1VendorNdlterm;
	}

	public static String getOfferData_T9(Connection con, String tenderId, String principal_ref_id){
		String mailBody = "";	
		DecimalFormat df2 = new DecimalFormat("#0.00");
		try{
			String mfilled = "";
			String cfilled = "";
			String c1filled = "";

			String formulaQuery = "SELECT M, C, C1 FROM EPROC.IOCL_LNG_PRODUCT_FORMULA_CONFIG WHERE REF_ID IN (SELECT FORMULA_CONFIG_ID FROM EPROC.IOCL_LNG_TENDER_PROPERTIES WHERE EVENT_ID = ? AND PRINCIPAL_REF_ID = ?) AND PRINCIPAL_REF_ID = ?";
			PreparedStatement ntpstmt = con.prepareStatement(formulaQuery);
			ntpstmt.setString(1,tenderId);
			ntpstmt.setString(2,principal_ref_id);
			ntpstmt.setString(3,principal_ref_id);
			ResultSet ntrs = ntpstmt.executeQuery();
			if(ntrs.next()){
				mfilled = ntrs.getString(1);
				cfilled = ntrs.getString(2);
				c1filled = ntrs.getString(3);
			}
			ntrs.close();ntpstmt.close();

			String prodUnitName ="";
			String unitQry="SELECT u.UNIT_NAME FROM IOCL_LNG_UNIT_MASTER u, IOCL_LNG_TENDER_PROPERTIES t WHERE t.EVENT_ID=? and t.UNIT_PRODUCT_ID=u.UNIT_ID AND t.PRINCIPAL_REF_ID=? and t.PRINCIPAL_REF_ID=u.PRINCIPAL_REF_ID";
			ntpstmt=con.prepareStatement(unitQry);
			ntpstmt.setString(1,tenderId);
			ntpstmt.setString(2,principal_ref_id);
			ntrs=ntpstmt.executeQuery();
			if(ntrs.next()){
				prodUnitName=ntrs.getString(1);
			}
			ntrs.close();ntpstmt.close();

			String indentDet = ""+
					"<table width='1377' border = '1'>"+
					"<tbody>"+
					"<tr style='background-color:#ffa64d;'>"+
					"<td width='77' style = 'text-align:center;'><p><strong>Row ID</strong></p></td>"+
					"<td width='250' style = 'text-align:center;'><p><strong>Delivery Term of L1 in Tender</strong></p></td>"+
					"<td width='250' style = 'text-align:center;'><p><strong>Best Offer in Tender</strong></p></td>"+
					"<td width='250' style = 'text-align:center;'><p><strong>Delivery Term of L1 after Auction</strong></p></td>"+
					"<td width='250' style = 'text-align:center;'><p><strong>Best Offer After Auction</strong></p></td>"+
					"</tr>";

			String getIndentsOfATender = "SELECT DISTINCT pr.ITEM_ID, pr.CARGO FROM IOCL_LNG_POST_BID_RANK pr,IOCL_LNG_INDENT_REFINERY r WHERE pr.EVENT_ID=? AND pr.RANK_NO = 1 AND pr.PRINCIPAL_REF_ID = ? AND pr.ITEM_ID=r.ITEM_ID AND r.STATUS='a' UNION ALL SELECT DISTINCT pr.ITEM_ID, pr.CARGO FROM IOCL_LNG_POST_BID_RANK pr WHERE pr.EVENT_ID=? AND pr.RANK_NO = 1 AND ITEM_ID = -1 ORDER BY 2";

			PreparedStatement pstInd = con.prepareStatement(getIndentsOfATender);
			pstInd.setString(1,tenderId);
			pstInd.setString(2,principal_ref_id);
			pstInd.setString(3,tenderId);
			ResultSet rsInd = pstInd.executeQuery();
			while(rsInd.next()){
				String bestOffer="";
				String itemId = rsInd.getString(1);
				String finalCargoLoc = rsInd.getString(2);

				indentDet = indentDet+
						"<tr>"+
						"<td width='77' style = 'text-align:center;'><p>"+finalCargoLoc+"</p></td>";

				String qry = "SELECT M, C, C1_RAW, B.DELIVERY_TERM FROM EPROC.IOCL_LNG_POST_BID_RANK A, IOCL_LNG_DELIVERY_TERM B WHERE A.DEL_TERM_ID = B.REF_ID AND A.EVENT_ID = ? AND A.ITEM_ID = ? AND RANK_NO = '1' ORDER BY LP, DP, Y, M, C, C1, A.REF_ID";
				PreparedStatement pst=con.prepareStatement(qry);
				pst.setString(1, tenderId);
				pst.setString(2, itemId);
				ResultSet rst=pst.executeQuery();
				if(rst.next()){						
					if("b".equals(mfilled)){
						bestOffer+="Slope (%)-"+df2.format(Double.parseDouble(rst.getString(1)))+"<br/>";
					}if("b".equals(cfilled)){
						bestOffer+="Price/Constant ("+prodUnitName+")-"+df2.format(Double.parseDouble(rst.getString(2)))+"<br/>";
					}if("b".equals(c1filled)){
						bestOffer+="Price/Constant (INR/"+prodUnitName.split("/")[1]+")-"+df2.format(Double.parseDouble(rst.getString(3)));
					}

					indentDet = indentDet+"<td width='70' style = 'text-align:center;'><p>"+rst.getString(4)+"</p></td><td width='70'><p>"+bestOffer+"</p></td>";
				}
				rst.close();pst.close();

				//GET OFFER FROM RA
				String minlp = "0";
				qry = "SELECT MIN(LP) FROM EPROC.IOCL_LNG_L1_BID WHERE  EVENT_ID=? AND CARGO=? GROUP BY CARGO";
				pst = con.prepareStatement(qry);
				pst.setString(1,tenderId);
				pst.setString(2,finalCargoLoc);
				rst = pst.executeQuery();
				if(rst.next()){
					minlp = rst.getString(1);
				}
				rst.close();pst.close();

				String ra_rank_ref_id = "0";
				qry = "SELECT A.RANK_REF_ID,A.Y,A.M_C_C1,A.BID_AMOUNT,A.DP,B.DELIVERY_TERM FROM EPROC.IOCL_LNG_RA_LAST_BID A, IOCL_LNG_DELIVERY_TERM B WHERE A.DEL_TERM_ID = B.REF_ID AND A.EVENT_ID=? AND A.CARGO=? AND A.LP="+minlp;
				pst = con.prepareStatement(qry);
				pst.setString(1,tenderId);
				pst.setString(2,finalCargoLoc);
				//pst.setString(3,minlp);
				rst = pst.executeQuery();
				if(rst.next()){
					ra_rank_ref_id = rst.getString(1);
					String M_C_C1 = rst.getString(3);
					if("M".equalsIgnoreCase(M_C_C1)){
						bestOffer="Slope (%)-"+df2.format(Double.parseDouble(rst.getString(4)))+"<br/>";
					}if("C".equalsIgnoreCase(M_C_C1)){
						bestOffer="Price/Constant ("+prodUnitName+")-"+df2.format(Double.parseDouble(rst.getString(4)))+"<br/>";
					}if("C1".equalsIgnoreCase(M_C_C1)){
						bestOffer="Price/Constant (INR/"+prodUnitName.split("/")[1]+")-"+df2.format(Double.parseDouble(rst.getString(4)));
					}
					indentDet = indentDet+"<td width='250' style = 'text-align:center;'><p>"+rst.getString(6)+"</p></td><td width='250'><p>"+bestOffer+"</p></td>";
				}else{
					indentDet = indentDet+"<td width='500' colspan='2'><p>No Revision</p></td>";
				}
				rst.close();pst.close();

				indentDet = indentDet+"</tr>";	
			}
			rsInd.close();pstInd.close();
			indentDet = indentDet+
					"</tbody>"+
					"</table>";

			mailBody = indentDet;
		}catch(Exception ex){
			System.out.print("getOfferData_T9:-"+ex);
			try{System.out.print("getOfferData_T9:-"+ex);}catch(Exception ggg){}		
		}
		return mailBody;
	}
	
	public static boolean auctionCompletionMail_42(Connection con, String tenderId,String annexureName, String principal_ref_id, REVMail email22, String fromMail, String mailMask, Map<String,String> toMailsMap, Map<String,String> ccMailsMap, String footer) throws Exception{
		try{
			String tenderNum="";
			String tenderPublishDate="";
			String toMailIds= StringUtils.join(toMailsMap.values(), ","); 
			String[] ccMailIds={}; if(ccMailsMap.size()>0)  ccMailIds=StringUtils.join(ccMailsMap.values(), ",").split(",");
			List<String> toUserIdsList=new ArrayList<String>();
			toUserIdsList.addAll(toMailsMap.keySet());

			String qry = "SELECT EVENT_NO,VARCHAR_FORMAT(EVENT_VIEW_TIME, 'DD.MM.YYYY') FROM EP_EVENT_MASTER WHERE REF_ID =?";
			PreparedStatement pstmt = con.prepareStatement(qry);  
			pstmt.setString(1,tenderId);
			ResultSet rs1=pstmt.executeQuery(); 
			if(rs1.next()){
				tenderNum = rs1.getString(1);
				tenderPublishDate = rs1.getString(2);
			}
			rs1.close();pstmt.close();

			boolean data_exist = false;
			if (annexureName.equalsIgnoreCase("annexure5")) {
				//qry = "SELECT DISTINCT A.ITEM_ID FROM IOCL_LNG_COUNTER_LAST_BID A,IOCL_LNG_INDENT_REFINERY B, IOCL_LNG_REFCUST C WHERE A.EVENT_ID=? AND A.ITEM_ID = B.ITEM_ID AND B.STATUS = 'a' AND B.REFINERY_ID = C.REF_CUST_ID AND C.STATUS = 'r'";
				qry = "SELECT DISTINCT A.ITEM_ID FROM IOCL_LNG_POST_BID_RANK A,IOCL_LNG_INDENT_REFINERY B, IOCL_LNG_REFCUST C WHERE A.EVENT_ID=? AND A.ITEM_ID = B.ITEM_ID AND B.STATUS = 'a' AND B.REFINERY_ID = C.REF_CUST_ID AND C.STATUS = 'r' AND A.RA_STAT='y'";
			}
			if (annexureName.equalsIgnoreCase("annexure6")) {
				//qry = "SELECT DISTINCT A.ITEM_ID FROM IOCL_LNG_COUNTER_LAST_BID A,IOCL_LNG_INDENT_REFINERY B, IOCL_LNG_REFCUST C WHERE A.EVENT_ID=? AND A.ITEM_ID = B.ITEM_ID AND B.STATUS = 'a' AND B.REFINERY_ID = C.REF_CUST_ID AND C.STATUS = 'c'";
				qry = "SELECT DISTINCT A.ITEM_ID FROM IOCL_LNG_POST_BID_RANK A,IOCL_LNG_INDENT_REFINERY B, IOCL_LNG_REFCUST C WHERE A.EVENT_ID=? AND A.ITEM_ID = B.ITEM_ID AND B.STATUS = 'a' AND B.REFINERY_ID = C.REF_CUST_ID AND C.STATUS = 'c' AND A.RA_STAT='y'";
			}
			pstmt = con.prepareStatement(qry);
			pstmt.setString(1, tenderId);
			rs1 = pstmt.executeQuery();
			if (rs1.next()) {
				data_exist = true;
			}
			rs1.close();
			pstmt.close();

			if (data_exist) {

				String subject="Electronic Procurement- Notification of Auction completion for providing Inputs (Tender ID- "+tenderNum+", Dated "+tenderPublishDate+")";

				String body="<p> Dear User,<br/><br/>The Reverse Auction for Tender ID- "+tenderNum+", Dated "+tenderPublishDate+" has been completed.<br/><br/>The details of the best offer received after Auction are as below:<br/><br/>";

				String tenderTable7_8="";

				if(annexureName.equalsIgnoreCase("annexure5")){
					tenderTable7_8=getRefineryWithLAP_T7(con, tenderId, principal_ref_id,"AFTER_RA")+"<br/>";
				}else{
					tenderTable7_8=getCustomerTable_T8(con, tenderId, principal_ref_id, "AFTER_RA")+"<br/>";
				}

				String body1="You are requested to provide the necessary Inputs for the Annexure- "+annexureName+" so that Price Acceptance can be done. <br/><br/>Tender Timelines are as below:<br/><br/>";

				String tenderTable4=getTenderTimelines_T4(con, tenderId, principal_ref_id,true)+"<br/>";

				String body2="In case of any clarifications or feedback, you may contact the concerned department.<br/><br/>";

				String mailBody = body+tenderTable7_8+body1+tenderTable4+body2+footer;

				qry = "INSERT INTO IOCL_LNG_REMAINDER_MAILS(EVENT_ID,PRINCIPAL_REF_ID,DESC,MAIL_STATUS,ENTRY_TIME) values (?,?,?,?,current timestamp)";
				pstmt = con.prepareStatement(qry);
				pstmt.setString(1, tenderId);
				pstmt.setString(2, principal_ref_id);
				pstmt.setString(3, "AUCTION_COMPLETION_T42");
				pstmt.setString(4, "y");
				int rem_cnt = pstmt.executeUpdate();

				String indentIds="";
						
				qry = "SELECT INDENT_ID, INDENT_NAME FROM IOCL_LNG_INDENT_MASTER WHERE EVENT_ID=? ORDER BY INDENT_ID";
				pstmt = con.prepareStatement(qry);  
				pstmt.setString(1,tenderId);
				rs1=pstmt.executeQuery(); 
				while(rs1.next()){
					if("".equals(indentIds)){
						indentIds=rs1.getString(1);
					}else{
						indentIds=indentIds+","+rs1.getString(1);
					}
				}
				rs1.close();pstmt.close();

				if (rem_cnt == 1) {
					//System.out.println("mailBody "+mailBody);
					
					boolean isMailLogCreatedUser = updateMailLogUserTender(toUserIdsList, principal_ref_id, tenderId,indentIds, mailBody, subject, "Notification of Auction completion for providing Inputs", toMailsMap, ccMailsMap);
					email22.sendMailWithMultiToWithName(fromMail, toMailIds, ccMailIds, subject, mailBody, mailMask);
					return true;
				} else {
					return false;
				}

			} else {
				System.out.println("auctionCompletionMail_42 no data found "+tenderId+" for "+annexureName);
				return false;
			}
		}catch(Exception ex){
			System.out.print("Exception in auctionCompletionMail_42 for tenderId "+tenderId+" : "+ex.toString());
			throw ex;
		}
	}

	public static boolean auctionCompletionMail_43(Connection con, String tenderId, String principal_ref_id, REVMail email22, String fromMail, String mailMask, Map<String,String> toMailsMap, Map<String,String> ccMailsMap, String footer) throws Exception{
		try{
			String tenderNum="";
			String tenderPublishDate="";
			String toMailIds= StringUtils.join(toMailsMap.values(), ","); 
			String[] ccMailIds={}; if(ccMailsMap.size()>0)  ccMailIds=StringUtils.join(ccMailsMap.values(), ",").split(",");
			List<String> toUserIdsList=new ArrayList<String>();
			toUserIdsList.addAll(toMailsMap.keySet());

			String qry = "SELECT EVENT_NO,VARCHAR_FORMAT(EVENT_VIEW_TIME, 'DD.MM.YYYY') FROM EP_EVENT_MASTER WHERE REF_ID =?";
			PreparedStatement pstmt = con.prepareStatement(qry);  
			pstmt.setString(1,tenderId);
			ResultSet rs1=pstmt.executeQuery(); 
			if(rs1.next()){
				tenderNum = rs1.getString(1);
				tenderPublishDate = rs1.getString(2);
			}
			rs1.close();pstmt.close();


			String subject="Electronic Procurement- Notification of Auction completion (Tender ID- "+tenderNum+", Dated "+tenderPublishDate+")";

			String body="<p> Dear User,<br/><br/>This is to inform that the Reverse Auction for Tender ID- "+tenderNum+", Dated "+tenderPublishDate+" has been completed.<br/><br/>The details of the best offer received after Auction are as below:<br/><br/>";

			String tenderTable9=getOfferData_T9(con, tenderId, principal_ref_id);

			String mailBody = body+tenderTable9+footer;

			qry = "INSERT INTO IOCL_LNG_REMAINDER_MAILS(EVENT_ID,PRINCIPAL_REF_ID,DESC,MAIL_STATUS,ENTRY_TIME) values (?,?,?,?,current timestamp)";
			pstmt = con.prepareStatement(qry);
			pstmt.setString(1, tenderId);
			pstmt.setString(2, principal_ref_id);
			pstmt.setString(3, "AUCTION_COMPLETION_T43");
			pstmt.setString(4, "y");
			int rem_cnt = pstmt.executeUpdate();

			String indentIds="";
						
			qry = "SELECT INDENT_ID, INDENT_NAME FROM IOCL_LNG_INDENT_MASTER WHERE EVENT_ID=? ORDER BY INDENT_ID";
			pstmt = con.prepareStatement(qry);  
			pstmt.setString(1,tenderId);
			rs1=pstmt.executeQuery(); 
			while(rs1.next()){
				if("".equals(indentIds)){
					indentIds=rs1.getString(1);
				}else{
					indentIds=indentIds+","+rs1.getString(1);
				}
			}
			rs1.close();pstmt.close();
				
			if (rem_cnt == 1) {
			//	System.out.println("mailBody "+mailBody);
				boolean isMailLogCreatedUser = updateMailLogUserTender(toUserIdsList, principal_ref_id, tenderId,indentIds, mailBody, subject, "Notification of Auction completion", toMailsMap, ccMailsMap);
				email22.sendMailWithMultiToWithName(fromMail, toMailIds, ccMailIds, subject, mailBody, mailMask);
				return true;
			} else {
				return false;
			}
		}catch(Exception ex){
			System.out.print("Exception in auctionCompletionMail_43 for tenderId "+tenderId+" : "+ex.toString());
			throw ex;
		}
	}

	public static Map<String, String> getTenderEvaluatorIds(Connection con, String principal_ref_id, String prodCat,
			String dept) {
		Map<String, String> idMailMap = new LinkedHashMap<String, String>();
		List<String> refIds = new ArrayList<String>();
		PreparedStatement psTender = null;
		ResultSet rs2 = null;
		try {
			String getIND = "SELECT PMR.CATEGORY,PMR.ROLES,PMR.PM_USER_ID,PM.USER_REF_ID,PM.EMAIL FROM EPROC.EP_PM_USERS PM,IOCL_LNG_PM_USERS_ROLES PMR WHERE PM.USER_REF_ID = PMR.PM_USER_ID AND PM.PRINCIPAL_REF_ID=? AND PM.ACTIVATE = 'y' AND PM.DSC_STATUS = 'y' AND PMR.DEPT=? AND (PMR.CATEGORY like '" + prodCat + "#%' OR PMR.CATEGORY like '%#" + prodCat+ "' OR PMR.CATEGORY like '%#" + prodCat + "#%' OR PMR.CATEGORY = ?) AND PMR.STATUS = 'a' AND PMR.PRINCIPAL_REF_ID = ?";
			psTender = con.prepareStatement(getIND);
			psTender.setString(1, principal_ref_id);
			psTender.setString(2, dept);
			psTender.setString(3, prodCat);
			psTender.setString(4, principal_ref_id);
			rs2 = psTender.executeQuery();
			while (rs2.next()) {
				String category_db[] = rs2.getString(1).split("#");
				String roles_db[] = rs2.getString(2).split("#");
				for (int i = 0; i < category_db.length; i++) {
					if (category_db[i].equals(prodCat)) {
						if (roles_db[i].contains("TEV,") || roles_db[i].contains(",TEV,") || roles_db[i].contains(",TEV") || roles_db[i].equals("TEV")) {
							String ref_id = rs2.getString(4);
							String mail_id = rs2.getString(5);
							refIds.add(ref_id);
							idMailMap.put(ref_id, mail_id);
						}
					}
				}
			}
			rs2.close();
			psTender.close();

		} catch (Exception ex) {
			System.out.print("getTenderEvaluatorIds mails:-" + ex);
		}
		return idMailMap;
	}

	public static Map<String, String> getTenderAdminIds(Connection con, String principal_ref_id, String prodCat) {
		Map<String, String> idMailMap = new LinkedHashMap<String, String>();
		List<String> refIds = new ArrayList<String>();
		PreparedStatement psTender = null;
		ResultSet rs2 = null;
		try {
			String getIND = "SELECT PMR.CATEGORY,PMR.ROLES,PMR.PM_USER_ID,PM.USER_REF_ID,PM.EMAIL FROM EPROC.EP_PM_USERS PM,IOCL_LNG_PM_USERS_ROLES PMR WHERE  PM.USER_REF_ID = PMR.PM_USER_ID AND PM.PRINCIPAL_REF_ID=? AND PM.ACTIVATE = 'y' AND PM.DSC_STATUS = 'y' AND (PMR.CATEGORY like '"+ prodCat + "#%' OR PMR.CATEGORY like '%#" + prodCat + "' OR PMR.CATEGORY like '%#" + prodCat+ "#%' OR PMR.CATEGORY = ?) AND PMR.STATUS = 'a' AND PMR.PRINCIPAL_REF_ID = ?";
			psTender = con.prepareStatement(getIND);
			psTender.setString(1, principal_ref_id);
			psTender.setString(2, prodCat);
			psTender.setString(3, principal_ref_id);
			rs2 = psTender.executeQuery();
			while (rs2.next()) {
				String category_db[] = rs2.getString(1).split("#");
				String roles_db[] = rs2.getString(2).split("#");
				for (int i = 0; i < category_db.length; i++) {
					if (category_db[i].equals(prodCat)) {
						if (roles_db[i].contains("TAD,") || roles_db[i].contains(",TAD,") || roles_db[i].contains(",TAD") || roles_db[i].equals("TAD")) {
							String ref_id = rs2.getString(4);
							String mail_id = rs2.getString(5);
							refIds.add(ref_id);
							idMailMap.put(ref_id, mail_id);
						}
					}
				}
			}
			rs2.close();
			psTender.close();

		} catch (Exception ex) {
			System.out.print("getTenderAdminIds mails:-" + ex);
		}
		return idMailMap;
	}


	public static Map<String,String> getFinanceDeptIds(Connection con, String principal_ref_id, String prodCat){
		Map<String,String> idMailMap = new LinkedHashMap<String,String>();
		PreparedStatement psTender=null;
		ResultSet rs2=null;
		try{
			String getIND = "SELECT PMR.CATEGORY,PMR.ROLES,PMR.PM_USER_ID,PM.USER_REF_ID,PM.EMAIL FROM EPROC.EP_PM_USERS PM,IOCL_LNG_PM_USERS_ROLES PMR WHERE  PM.USER_REF_ID = PMR.PM_USER_ID AND PM.PRINCIPAL_REF_ID=? AND PM.ACTIVATE = 'y' AND PM.DSC_STATUS = 'y' AND (PMR.CATEGORY like '"+prodCat+"#%' OR PMR.CATEGORY like '%#"+prodCat+"' OR PMR.CATEGORY like '%#"+prodCat+"#%' OR PMR.CATEGORY = ?) AND PMR.DEPT IN(SELECT DEPT_MGMT_ID FROM EPROC.IOCL_LNG_DEPT_MGMT WHERE DEPT_MGMT_NAME ='Finance' AND PRINCIPAL_REF_ID=88 ) and PMR.STATUS = 'a' AND PMR.PRINCIPAL_REF_ID = ?";
			psTender=con.prepareStatement(getIND);
			psTender.setString(1,principal_ref_id);
			psTender.setString(2,prodCat);
			psTender.setString(3,principal_ref_id);
			rs2=psTender.executeQuery();
			while(rs2.next()){			
				idMailMap.put(rs2.getString(4),rs2.getString(5));	
			}
			rs2.close();
			psTender.close();
		}catch(Exception ex){
			System.out.print("getFinanceDeptIds mails:-"+ex);
		}
		return idMailMap;
	}

	public static void main(String[] args) {
		Connection con = null;
		//connect cm=new connect();
		ConnectionMaker cm=new ConnectionMaker();

		String principal_ref_id = "88";
		String fromMail = "admin@mstcecommerce.com";
		String mailMask = "IndianOil : Gas Import";
		String footer = "<p>Regards,<br/>For and on behalf of <br/> Indian Oil Corporation Limited <br/> Business Development (Gas)<br></p><p>&nbsp;</p><p>Note: - &quot;This is a system generated message. Please do not respond to the same.&quot;</p>";

		try{
			con =cm.getConnection("eproc"); 
		}catch(Exception exception){
			System.out.println("IOCL LNG AUCTION COMPLETION MAIL Connection -- :" + exception);
		}
		try{
			con.setAutoCommit(false);
			REVMail email = new REVMail();
			email.setSmtpServer("10.1.5.30");
			System.out.println("*** IOCL LNG AUCTION COMPLETION MAIL Current Time :  " + new java.util.Date() + "***");
			List<String> events = new ArrayList<String>();

			Statement statement = con.createStatement();
			ResultSet resultset = statement.executeQuery("SELECT DISTINCT REF_ID FROM EPROC.EP_EVENT_MASTER WHERE PRINCIPAL_REF_ID = "+principal_ref_id+" AND REF_ID IN (SELECT DISTINCT EVENT_ID FROM EPROC.IOCL_LNG_POST_BID_RANK WHERE RA_STAT = 'y') AND PBMEET_END_TIME < CURRENT TIMESTAMP AND REF_ID NOT IN(SELECT DISTINCT EVENT_ID FROM IOCL_LNG_REMAINDER_MAILS WHERE PRINCIPAL_REF_ID = "+principal_ref_id+ " AND DESC IN ('AUCTION_COMPLETION_T42','AUCTION_COMPLETION_T43'))");
			while (resultset.next()) {
				String eventId = resultset.getString(1);
				events.add(eventId);
			}
			resultset.close();statement.close();

			for(int i=0;i<events.size();i++){
				String eventId = events.get(i);
				System.out.println("***IOCL LNG AUCTION COMPLETION MAIL eventId:" + eventId);
				Statement stmt = null;
				ResultSet rs = null;
				String prodCatId = "";

				/*Mail Management Point No 42--Electronic Procurement Notification of Auction completion for providing Inputs(IOCL USERS)*/
				// MAIL LOGIC ANNEX 5
				String makerDeptId = "0";
				Map<String, String> toMailsMap = new HashMap<String, String>();
				Map<String, String> ccMailsMap = new HashMap<String, String>();

				Map<String, String> toMailsMap_annx6 = new HashMap<String, String>();
				String makerDeptId_annx6 = "0";

				String qry = "SELECT MAKER_DEPARTMENT_ID FROM IOCL_LNG_ASSIGNMENT WHERE EVENT_ID=? AND ANNEXURE_NO=5 AND STATUS='a' AND PRINCIPAL_REF_ID=?";
				PreparedStatement pstmt = con.prepareStatement(qry);
				pstmt.setString(1, eventId);
				pstmt.setString(2, principal_ref_id);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					makerDeptId = rs.getString(1);
				}
				rs.close();
				pstmt.close();

				stmt = con.createStatement();
				rs = stmt.executeQuery("SELECT CAT_ID FROM IOCL_LNG_TENDER_SNAPSHOT WHERE EVENT_ID=" + eventId+" AND PRINCIPAL_REF_ID="+principal_ref_id);
				if (rs.next()) {
					prodCatId = rs.getString(1);
				}
				rs.close();stmt.close();

				Map<String, String> tenderEvaluatorIdsAnnx5 = getTenderEvaluatorIds(con, principal_ref_id, prodCatId, makerDeptId);
				Map<String, String> tenderAdminIds = getTenderAdminIds(con, principal_ref_id, prodCatId);
				toMailsMap.putAll(tenderEvaluatorIdsAnnx5);
				ccMailsMap.putAll(tenderAdminIds);
				System.out.println("***IOCL LNG AUCTION COMPLETION MAIL NO. 42 annexure5 toMailsMap : " + toMailsMap+" ccMailsMap: "+ccMailsMap);
				auctionCompletionMail_42(con, eventId,"annexure5", principal_ref_id, email, fromMail, mailMask, toMailsMap, ccMailsMap, footer);
				System.out.println("***IOCL LNG AUCTION COMPLETION MAIL NO. 42 annexure5 sent successfully");

				// Annexure 6
				qry = "SELECT MAKER_DEPARTMENT_ID FROM IOCL_LNG_ASSIGNMENT WHERE EVENT_ID=? AND ANNEXURE_NO=6 AND STATUS='a' AND PRINCIPAL_REF_ID=?";
				pstmt = con.prepareStatement(qry);
				pstmt.setString(1, eventId);
				pstmt.setString(2, principal_ref_id);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					makerDeptId_annx6 = rs.getString(1);
				}
				rs.close();
				pstmt.close();

				toMailsMap_annx6.putAll(getTenderEvaluatorIds(con, principal_ref_id, prodCatId, makerDeptId_annx6));
				System.out.println("***IOCL LNG AUCTION COMPLETION MAIL NO. 42 annexure6 toMailsMap_annx6 : " + toMailsMap_annx6+" ccMailsMap: "+ccMailsMap);
				auctionCompletionMail_42(con, eventId,"annexure6", principal_ref_id, email, fromMail, mailMask, toMailsMap_annx6, ccMailsMap, footer);
				System.out.println("***IOCL LNG AUCTION COMPLETION MAIL NO. 42 annexure6 sent successfully");


				/*Mail Management Point No 43--Electronic Procurement Notification of Auction completion (IOCL USERS)*/
				int deptId = 0;
				qry = "SELECT PRICE_ACCEPTOR_ID FROM IOCL_LNG_PRICE_ACCEPTOR WHERE EVENT_ID = ? AND PRINCIPAL_REF_ID =?";
				pstmt = con.prepareStatement(qry);
				pstmt.setString(1, eventId);
				pstmt.setString(2, principal_ref_id);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					deptId = rs.getInt(1);
				}
				rs.close();
				pstmt.close();
				Map<String, String> toMailsMap1 = new HashMap<String, String>();
				Map<String, String> ccMailsMap1 = new HashMap<String, String>();
				toMailsMap1.putAll(getFinanceDeptIds(con, principal_ref_id, prodCatId));
				ccMailsMap1.putAll(tenderAdminIds);
				ccMailsMap1.putAll(getPriceAcceptorIdsByDept(con, principal_ref_id, prodCatId,deptId));				
				System.out.println("***IOCL LNG AUCTION COMPLETION MAIL NO. 43 toMailsMap1 : " + toMailsMap1+" ccMailsMap1: "+ccMailsMap1);
				auctionCompletionMail_43(con, eventId, principal_ref_id, email, fromMail, mailMask, toMailsMap1, ccMailsMap1, footer);
				System.out.println("***IOCL LNG AUCTION COMPLETION MAIL NO. 43 sent successfully");
			}

			con.commit();
		} catch (Exception exception1) {
			System.out.println("IOCL LNG AUCTION COMPLETION MAIL Exception in main :"+exception1);
			try {
				con.rollback();
			} catch (SQLException e) {
				System.out.println("Error2 " + e);
			}
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("Error3 " + e);
			}
		}


	}

}
