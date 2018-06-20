package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*There are total 4 DataMungerTest file:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 4 methods
 * a)getBaseQuery()  b)getFileName()  c)getOrderByClause()  d)getGroupByFields()
 * 
 * Once you implement the above 4 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 2 methods
 * a)getFields() b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getRestrictions()  b)getLogicalOperators()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();

	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
        queryParameter.setFileName(getFileName(queryString));
        queryParameter.setBaseQuery(getBaseQuery(queryString));
        queryParameter.setOrderByFields(getOrderByFields(queryString));
        queryParameter.setGroupByFields(getGroupByFields(queryString));
        queryParameter.setFields(getFields(queryString));
        queryParameter.setAggregateFunctions(getAggregateFunctions(queryString));
        queryParameter.setLogicalOperators(getLogicalOperators(queryString));
      //  queryParameter.setQUERY_TYPE(qUERY_TYPE);
        queryParameter.setRestrictions(getConditions(queryString));
		return queryParameter;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after the
	 * "from" clause.
	 */
	public String getFileName(String queryString) {
		
		/*	
		 * Logic -- First split the string on from(keyword). Take the 2nd part after
		 * from  i.e ipl.csv group by city. Now split the second string on " " and check
		 * for each string which contains .csv in it. Return the string.
	     */
			
			String[] wordSplit=queryString.toLowerCase().split(" from ");
			String afterFrom=wordSplit[1];
			String[] splitAfterFrom=afterFrom.split(" ");
			String fileName="";
			for(int i=0;i<splitAfterFrom.length;i++) {
				if(splitAfterFrom[i].contains(".csv"))
					fileName=fileName+splitAfterFrom[i];
			}
			return fileName;
		}
	/*
	 * 
	 * Extract the baseQuery from the query.This method is used to extract the
	 * baseQuery from the query string. BaseQuery contains from the beginning of the
	 * query till the where clause
	 */
	public String getBaseQuery(String queryString) {
		
		 /*
		  * Logic -- First get the indexof where. if where is present in the string
		  * then split on where and return the 0th string of the string array.
		  * If where is not present then check for index of groupby and orderby in else
		  * case. If groupby orderby both index is -1 return the queryString as it cause
		  * it has no where no groupby and no orderby. If one of the groupby and orderby 
		  * present then split on it and return the 0th string of the string array.
		  * If both groupby and orderby is present then split on value whose index is small
		  * (means split on that which comes first) and return the 0th string from the string array.
		  */
			int indexOfWhere=queryString.toLowerCase().indexOf(" where ");
			if(indexOfWhere!=-1) {
				String[] splitOnWhere=queryString.toLowerCase().split(" where ");
				return splitOnWhere[0].trim();
			}
			else {
				int indexOfGroupBy=queryString.toLowerCase().indexOf(" group by ");
				int indexOfOrderBy=queryString.toLowerCase().indexOf(" order by ");
				if(indexOfGroupBy ==-1 && indexOfGroupBy==-1) {
					return queryString.toLowerCase();
				}
				else if(indexOfGroupBy == -1 && indexOfOrderBy !=-1) {
					String[] splitOnOrderBy=queryString.toLowerCase().split(" order by ");
					return splitOnOrderBy[0];
				}
				else if(indexOfGroupBy != -1 && indexOfGroupBy ==-1) {
					String[] splitOnGroupBy=queryString.toLowerCase().split(" group by ");
					return splitOnGroupBy[0];
				}
				else {
					if(indexOfGroupBy<indexOfOrderBy) {
						String[] splitOnGroupBy=queryString.toLowerCase().split(" group by ");
						return splitOnGroupBy[0];
					}
					else {
						String[] splitOnGroupBy=queryString.toLowerCase().split(" order by ");
						return splitOnGroupBy[0];
					}
				}
			}
		}
	/*
	 * extract the order by fields from the query string. Please note that we will
	 * need to extract the field(s) after "order by" clause in the query, if at all
	 * the order by clause exists. For eg: select city,winner,team1,team2 from
	 * data/ipl.csv order by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one order by fields.
	 */
	public List<String> getOrderByFields(String queryString) {
		
		/* Logic -- First check the index of order by if it is -1 than return null.
		 * Else split the string on order by and take the 2nd part. Split the second 
		 * part on comma and trim every string in it and then return the string array.
		 */
			String lowerCaseQueryString=queryString.toLowerCase();
			int orderByIndex=lowerCaseQueryString.indexOf(" order by ");
			if(orderByIndex==-1) {
				return null;
			}
			else {
				String[] orderBySplit=lowerCaseQueryString.split(" order by ");
				String afterOrderBy=orderBySplit[1].trim();
				String[] afterOrderByFields=afterOrderBy.split(",");
				for(int i=0;i<afterOrderByFields.length;i++) {
					afterOrderByFields[i]=afterOrderByFields[i].trim();
				}
				ArrayList<String> al=new ArrayList<String>();
				for(int i=0;i<afterOrderByFields.length;i++) {
					al.add(afterOrderByFields[i]);
				}
				
				return al;
			}

		}

	/*
	 * Extract the group by fields from the query string. Please note that we will
	 * need to extract the field(s) after "group by" clause in the query, if at all
	 * the group by clause exists. For eg: select city,max(win_by_runs) from
	 * data/ipl.csv group by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one group by fields.
	 */
	public List<String> getGroupByFields(String queryString) {
		
		/* Logic -- First check the index of order by if it is -1 than return null.
		 * Else split the string on order by and take the 2nd part. Split the second 
		 * part on comma and trim every string in it and then return the string array.
		 */
			String lowerCaseQueryString=queryString.toLowerCase();
			int groupByIndex=lowerCaseQueryString.indexOf(" group by ");
			if(groupByIndex==-1) {
				return null;
			}
			else {
				String[] groupBySplit=lowerCaseQueryString.split(" group by ");
				String afterGroupBy=groupBySplit[1].trim();
				int orderByIndex=afterGroupBy.indexOf(" order by ");
				if(orderByIndex==-1) {
					String[] afterGroupByFields=afterGroupBy.split(",");
					for(int i=0;i<afterGroupByFields.length;i++) {
						afterGroupByFields[i]=afterGroupByFields[i].trim();
					}
					ArrayList<String> alGroupBy=new ArrayList<String>();
					for(int i=0;i<afterGroupByFields.length;i++) {
						alGroupBy.add(afterGroupByFields[i]);
					}
					
					return alGroupBy;
					
				}
				else {
					String[] beforeOrderBy=afterGroupBy.split(" order by ");
					String beforeOroupByString=beforeOrderBy[0].trim();
					String[] afterGroupByFields=beforeOroupByString.split(",");
					for(int i=0;i<afterGroupByFields.length;i++) {
						afterGroupByFields[i]=afterGroupByFields[i].trim();
					}
					ArrayList<String> alGroupBy=new ArrayList<String>();
					for(int i=0;i<afterGroupByFields.length;i++) {
						alGroupBy.add(afterGroupByFields[i]);
					}
					
					return alGroupBy;
					
					
				}
			
				
			}
		}

	/*
	 * Extract the selected fields from the query string. Please note that we will
	 * need to extract the field(s) after "select" clause followed by a space from
	 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
	 * query mentioned above, we need to extract "city" and "win_by_runs". Please
	 * note that we might have a field containing name "from_date" or "from_hrs".
	 * Hence, consider this while parsing.
	 */
	public List<String> getFields(String queryString) {

	    /*	Logic -- To get all fields names, first split on from keyword. Take the first part
	     *  of the string array and then split on select keyword. Take the second part of string array
	     *  which is the actual string of fields. Then finally split on commas, trim the string and return 
	     *  the string array.
	     */
			String[] splitOnFrom=queryString.toLowerCase().split(" from ");
			String[] splitOnSelect=splitOnFrom[0].trim().split("select ");
			String[] splitOnComma=splitOnSelect[1].trim().split(",");
			List<String> al=new ArrayList<String>();
			for(int i=0;i<splitOnComma.length;i++) {
				splitOnComma[i]=splitOnComma[i].trim();
				al.add(splitOnComma[i]);
			}
			return al;
		}

	/*
	 * Extract the conditions from the query string(if exists). for each condition,
	 * we need to capture the following: 1. Name of field 2. condition 3. value
	 * 
	 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
	 * where season >= 2008 or toss_decision != bat
	 * 
	 * here, for the first condition, "season>=2008" we need to capture: 1. Name of
	 * field: season 2. condition: >= 3. value: 2008
	 * 
	 * the query might contain multiple conditions separated by OR/AND operators.
	 * Please consider this while parsing the conditions.
	 * 
	 */
	public String getConditionsPartQuery(String queryString) {
		
	    /*
	     * Logic-- First get the indexOf where if where is not there then no conditions will be
	     * there so return null. If where keyword is there than split on it and take the 2nd part
	     * of it cause condition will be there in second part.Now if group by order by keyword is
	     * not there in 2nd half of string then simply returns the 2nd half. If group by order by 
	     * is there than based on the index split the string and take the first part of it that will
	     * be our conditional string, return it.
	     */			
			int indexOfWhere=queryString.indexOf(" where ");
			if(indexOfWhere==-1) {
				return null;
			}
			else {
				String[] whereSplit=queryString.split("where");
				String afterWhere=whereSplit[1].trim();
				int indexOfGroupBy=afterWhere.indexOf("group by");
				int indexOfOrderBy=afterWhere.indexOf("order by");
				if(indexOfGroupBy==-1 && indexOfOrderBy==-1) {
					return afterWhere;
				}
				else if(indexOfGroupBy == -1 && indexOfOrderBy !=-1) {
					String[] splitOnOrderBy=afterWhere.split(" order by ");
					return splitOnOrderBy[0];
				}
				else if(indexOfGroupBy != -1 && indexOfGroupBy ==-1) {
					String[] splitOnGroupBy=afterWhere.split(" group by ");
					return splitOnGroupBy[0];
				}
				else {
					if(indexOfGroupBy<indexOfOrderBy) {
						String[] splitOnGroupBy=afterWhere.split(" group by ");
						return splitOnGroupBy[0];
					}
					else {
						String[] splitOnGroupBy=afterWhere.split(" group by ");
						return splitOnGroupBy[0];
					}
				}
			}
		}
	public List<Restriction> getConditions(String queryString) {
		
	     /*
	      *  Logic -- Pass the queryString to the getConditionPartQuery to get our condition as a String.
	      * If the where keyword is not there then condition string will be null hence return null. else
	      * split the string on (and or keyword). The main idea here is we are spliting on (space and space) 
	      * not just (and) because some field name may also contain ...and... as there substring.
	      */
			String conditions=getConditionsPartQuery(queryString);
			if(conditions==null) {
				return null;
			}
			else {
				String[] conditionArray=conditions.split(" and | or ");
				List<Restriction> al=new ArrayList<Restriction>();
				for(int i=0;i<conditionArray.length;i++) {
					String[] spaceSplit=conditionArray[i].split(" |'");
					String name=spaceSplit[0].trim();
					String operator=spaceSplit[1].trim();
					String value=spaceSplit[2].trim();
					Restriction r=new Restriction(name,value,operator);
							al.add(r);
				}
				return al;
			}

		}

	/*
	 * Extract the logical operators(AND/OR) from the query, if at all it is
	 * present. For eg: select city,winner,team1,team2,player_of_match from
	 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
	 * bangalore
	 * 
	 * The query mentioned above in the example should return a List of Strings
	 * containing [or,and]
	 */
	public List<String> getLogicalOperators(String queryString) {
	      
		/*
        *  Logic -- First check the indexOf where if index is -1 then no where no conditions
        * hence no operators return null. If where is there then split on where and take the
        * 2nd part of the string which contains our Logical operator. Split the second part
        * of the string on space and check for each string of the array whether it matches 
        * with the and ,or ,not . If it does put it in arrayList of string and finally copy
        * the value from arrayList to string array.
        * WHY CHOOSE ARRAYLIST -- as we don't know the length of the string array hence it is
        * better to take dynamic arrayList and proceed.
        * */
		int indexOfWhere= queryString.toLowerCase().indexOf(" where ");
		if(indexOfWhere==-1) {
			return null;
		}
		else {
			String lowerQueryString=queryString.toLowerCase();
			String[] afterWhere=lowerQueryString.split(" where ");
			String[] stringArray=afterWhere[1].split(" ");
			ArrayList<String> al=new ArrayList<String>();
			for(int i=0;i<stringArray.length;i++) {
				if(stringArray[i].equals("and") || stringArray[i].equals("or")|| stringArray[i].equals("not")) {
					al.add(stringArray[i]);
				}
			}
//			String[] returnAndOr=new String[al.size()];
//			for(int i=0;i<al.size();i++) {
//				returnAndOr[i]=al.get(i);
//			}
			return al;
		}
		
	}
	/*
	 * Extract the aggregate functions from the query. The presence of the aggregate
	 * functions can determined if we have either "min" or "max" or "sum" or "count"
	 * or "avg" followed by opening braces"(" after "select" clause in the query
	 * string. in case it is present, then we will have to extract the same. For
	 * each aggregate functions, we need to know the following: 1. type of aggregate
	 * function(min/max/count/sum/avg) 2. field on which the aggregate function is
	 * being applied.
	 * 
	 * Please note that more than one aggregate function can be present in a query.
	 * 
	 * 
	 */
	public List<AggregateFunction> getAggregateFunctions(String queryString) {
		/* Logic -- Our aggregate function will be between select and from keyword so 
		 * first split on the basis of from and take first string. Split the first half 
		 * on select keyword. Hence our aggregate function may lie on aggregateAssume string
		 * now split it on comma and for each string array check if it start with "sum("...
		 * If it does add it to arrayList. If arrayList is empty means no aggregate function
		 * return null else copy from arrayList to string array and return the array */
			String[] splitOnWhere=queryString.toLowerCase().split(" from ");
			String[] splitOnSelect=splitOnWhere[0].trim().split("select ");
			String aggregateAssume=splitOnSelect[1];
			String[] aggregateAssumeCommaSeparate=aggregateAssume.trim().split(",");
			ArrayList<String> al=new ArrayList<String>();
			for(int i=0;i<aggregateAssumeCommaSeparate.length;i++) {
				if(aggregateAssumeCommaSeparate[i].startsWith("sum(") || aggregateAssumeCommaSeparate[i].startsWith("count(")
						|| aggregateAssumeCommaSeparate[i].startsWith("min(") || aggregateAssumeCommaSeparate[i].startsWith("max(")
						|| aggregateAssumeCommaSeparate[i].contains("avg("))
				{
					al.add(aggregateAssumeCommaSeparate[i].trim());
				}
			}
			if(al.size()==0) {
				return null;
			}
			else {
			
			List<AggregateFunction> objectAggregate=new ArrayList<AggregateFunction>();
				for(int i=0;i<al.size();i++) {
					String aggre=al.get(i);
					String[] arrayAggre=aggre.split("\\(|\\)");
					String field=arrayAggre[1];
					String function=arrayAggre[0];
					AggregateFunction af=new AggregateFunction(field,function);
					
					objectAggregate.add(af);
				}
				return objectAggregate;
			}

		}
	public static void main(String[] args) {
		QueryParser qs=new QueryParser();
		List<Restriction>objectAggregate=qs.getConditions("select winner,season,team1,team2 from ipl.csv where season > 2014 and city ='Banglore'");
		for(int i=0;i<objectAggregate.size();i++) {
			System.out.println(objectAggregate.get(i).getName()+"\n"+objectAggregate.get(i).getValue()+"\n "+objectAggregate.get(i).getCondition());
		}
		
	}

}