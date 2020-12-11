<%@page import="org.json.JSONArray"%>
<%@page import="csci310.service.GraphJSONhelper.Data_and_Labels"%>
<%@page import="csci310.service.GraphJSONhelper"%>
<%@page import="java.util.Calendar"%>
<%@page import="csci310.service.Resolution"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="csci310.service.GraphingModule"%>
<%@page import="csci310.service.DatabaseClient"%>
<%@page import="csci310.model.Portfolio"%>
<%@page import="csci310.model.Stock"%>
<%@page import="java.util.ArrayList"%>
<%@page import="csci310.service.HomePageModule"%>
<%@ page import="csci310.*" isELIgnored="false"%>

    
<%
	HttpSession s = request.getSession();
	if(s.getAttribute("login") == "false" || s.getAttribute("login") == null) {
		response.sendRedirect("signIn.jsp");

	}
%>

<html>
<head>
	<title>Home</title>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/style.css">
	<link href="https://fonts.googleapis.com/css2?family=Cairo:wght@200;400;700;900&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=Teko:wght@200;400;700;900&display=swap" rel="stylesheet">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	<link href="https://bootstrap-confirmation.js.org/assets/css/docs.min.css" rel="stylesheet">
  	<link href="https://bootstrap-confirmation.js.org/assets/css/style.css" rel="stylesheet">
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	
	<script src="https://kit.fontawesome.com/dbcc9507e2.js" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/chart.js@2.9.3/dist/Chart.min.js" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/hammerjs@2.0.8/hammer.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-zoom@0.7.5/dist/chartjs-plugin-zoom.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap-confirmation2/dist/bootstrap-confirmation.min.js"></script>
	
	<script>
	// Function to enable datepicker plugin
	$( function() {
	  $( ".datepicker" ).datepicker();
	} );
	</script>
	
	<script>
		// Log out user after 120 seconds of inactivity
		var userInactivity = function () {
		    var time;
		    window.onload = resetTimer;
		    // Reset timer whenever user moves mouse or presses key
		    document.onmousemove = resetTimer;
		    document.onkeypress = resetTimer;
	
		    function logout() {
		    	// Alert user logout
		        alert("You have been logged out due to two minutes of inactivity.")
		        
		        let httpRequest = new XMLHttpRequest();
		        httpRequest.open("POST", "api/sio" , true);

		        // We will get alerted when backend gives back some kind of response
		        httpRequest.onreadystatechange = function(){
		          // This function runs when we get some kind of response back from iTunes
		          console.log(httpRequest);
		          // When we get back a DONE state (readyState == 4, let's do something with it)
		          if(httpRequest.readyState == httpRequest.DONE) {
		            // Check for errors - status code 200 means success
		            if(httpRequest.status == 200) {
		              console.log(httpRequest.responseText);

		              // Display the results on the browser - a separate function is created for this purpose
		              window.location.href = "signIn.jsp";

		            }
		            else {
		              console.log("AJAX Error!!");
		              console.log(httpRequest.status);
		              console.log(httpRequest.statusText);
		            }
		          }
		        }
		        httpRequest.send(); 
		        
		    }
		    // Reset the timer
		    function resetTimer() {
		        clearTimeout(time);
		        time = setTimeout(logout, 120000)
		    }
		};
		// Load timer on page load
		window.onload = function() {
			userInactivity(); 
		}
	</script>
	
</head>
<body>
    <% 
    	HomePageModule homePageModule = (HomePageModule) request.getSession().getAttribute("module"); 
        DatabaseClient db = new DatabaseClient();
        ArrayList<Stock> stockList = homePageModule.getStockList(); // owned stock
        
        // Get stock to graph map
        Map<Stock, Boolean> stockToGraphMap = homePageModule.getStockToGraphMap();
        Map<Stock, Boolean> viewedMap = homePageModule.getViewedStockPortfolioMap();
        // change format to xxx.xx
        
        // Get the arrayList which we will calculate the stock value
        ArrayList<Stock> stockToCal = new ArrayList<Stock>();
        for (Stock stock : stockToGraphMap.keySet()){
        	if (stockToGraphMap.get(stock)){
        		stockToCal.add(stock);
        	}
        }
        double percent = ((int) (homePageModule.getChangePercentDouble(stockToCal) * 10000)) / 100.0;        
        
        // Change format to xxx.xx
        double portfolioValue = (int) (homePageModule.getPortfolioValue() * 100) / 100.0;
        
        // Calculate graph data
        // Calculate start time and current time
        long curr_time = System.currentTimeMillis() / 1000L;
        String tp =(String) session.getAttribute("tp");
		Calendar date= Calendar.getInstance();
		Resolution r = Resolution.Weekly;
		
		if(tp==null)
	    {
			date.add(Calendar.MONTH, -3); 
	    }
	    else if(tp.equals("1"))
	    {
	    	date.add(Calendar.DATE, -4); 
	    	r = Resolution.Daily;
	    }
	    else if(tp.equals("2"))
	    {
	    	date.add(Calendar.DATE, -9); 
	    	r = Resolution.Daily;
	    }
	    else if(tp.equals("3"))
	    {
	    	date.add(Calendar.MONTH, -1); 
	    	r = Resolution.Weekly;
	    }
	    else if(tp.equals("4"))
	    {
	    	date.add(Calendar.MONTH, -12); 
	    	r = Resolution.Monthly;
	    }
	    
	    long start_time =  date.getTimeInMillis() / 1000L;
	    
	    String maybe_start = (String) session.getAttribute("start_date");
	    String maybe_end = (String) session.getAttribute("end_date");
	    
	    if(maybe_start!=null && maybe_end!=null)
	    {
	    	long start = Long.parseLong(maybe_start);
	    	long end = Long.parseLong(maybe_end);
	    	
	    	start_time = start/1000L;
	    	curr_time = end/1000L;
	    	
	    	long day_diff = (end - start)/(60*60*24*1000);
	    	
	    	// System.out.println("Day diff is = " + day_diff);
	    	
	    	if(day_diff <=14)
	    	{
	    		r = Resolution.Daily;
	    	}
	    	else if(day_diff <=100)
	    	{
	    		r = Resolution.Weekly;
	    	}
	    	else
	    	{
	    		r = Resolution.Monthly;
	    	}
	    }
	    
	    session.setAttribute("GStart", start_time);
	    
	    // System.out.println("start time = " + start_time);
	    
        int userID = (int) session.getAttribute("userID");
        Portfolio Current_user_view_portfolio = new Portfolio();
        for (Stock stock : viewedMap.keySet()){
        	if (viewedMap.get(stock)){
        		Current_user_view_portfolio.addStock(stock); // prepare for viewed stock graph
        	}
        }
        GraphingModule GMM = new GraphingModule();
        
        Portfolio portfolioToGraph = new Portfolio();
        for (Stock stock : stockToGraphMap.keySet()){
        	if (stockToGraphMap.get(stock)){
        		portfolioToGraph.addStock(stock);
        	}
        }
		Map<Date, Double> portfolio_info = GMM.getPortfolioValue(portfolioToGraph, r, start_time,curr_time);
		
		// Parse owned stock portfolio info into string
		GraphJSONhelper GJH = new GraphJSONhelper();
		Data_and_Labels Dn_L = GJH.Total_portfolio_Info(portfolio_info);
		String main_portfolio_json = Dn_L.Data_Json;
				
		ArrayList<String> userGraphInfo = new ArrayList<String>();	
		String Labels = "";
		Labels = Dn_L.Labels;
		if(db.getPortfolio(userID).getSize() <=0) //if the user has no portfolio, then dont append portfolio info into graph
		{
			//System.out.println("empty portfolio");
		}
		else
		{
		    userGraphInfo.add(main_portfolio_json);
		    // Initialize labels
		}
        // Calculate labels
        boolean first_time = true; //need to set labels only once. The helper function returns a pair of Data points JSON and Labels JSON.
				
		//Fills the formatted JSON of Graphing point array list with all Data points of all viewed stocks
		for(Stock stock: Current_user_view_portfolio.getPortfolio())
		{
			GraphJSONhelper G = new GraphJSONhelper();
			Data_and_Labels DnL = G.StockGraphInfo(stock.getTicker(), stock.getQuantity(), r, start_time, curr_time); 
			userGraphInfo.add(DnL.Data_Json);
			
			if(first_time)
			{
				Labels = DnL.Labels;
				first_time = false;
			}
		}
        
		String snp = (String)session.getAttribute("snp");
		
		if(snp!=null)
		{
			GraphJSONhelper GG = new GraphJSONhelper();
			Data_and_Labels DnL = GG.StockGraphInfo("SPY", 1, r, start_time, curr_time); 
			userGraphInfo.add(DnL.Data_Json);
			if(Labels.equals(""))
				Labels = DnL.Labels;
		}
        
		String GraphData = new JSONArray(userGraphInfo).toString();
		
        // Calculate isgraph
        boolean isGraph = Labels.equals("") ? false : true;
    %>
	<div class="navbar">
		<div class="wrap">
			<h1>USC CS 310: Stock Portfolio Management</h1>
			<button id="signOutB" class="button"><i class="fas fa-sign-out-alt"></i>&nbsp&nbspSign Out</button>
		</div>
	</div>
    <div class="wrap">
    	<div class="grid-container">
    	
	    	<div class="homepage-container" id="graph-container">
	    		<div class="graph-header">
	    			<span id="portfolio-value">$<%=portfolioValue%></span>
	    			<div id="portfolio-value-change" style="color: #51C58E;">
	    			    <% if (percent >= 0) {%>
				            <span id="arrow">&#9650 +<%=percent %>% Today</span>
				    	<% } %>
    					<% if (percent < 0) {%>
				            <span id="arrow2" style="color:red;"">&#9660 <%=percent %>% Today</span>
				    	<% } %>
	    			</div>
	    			<div>
	    			<button class="btn" id="zoomin">+</i></button>
	    			<button class="btn" id = "zoomout">-</button>
	    			</div>
	    		</div>

	    		<form name="GraphButtons" method="post" action="/api/GraphButtons">
	     		<div class="row justify-content-center" role="group" aria-label="Basic example">
				  <input type="submit" id="1-day-btn" class="btn btn-secondary" name="timePeriod" value="1D"/>
				  <input type="submit" id="1-week-btn" class="btn btn-secondary" name="timePeriod" value="1W"/>
				  <input type="submit" id="1-month-btn" class="btn btn-secondary" name="timePeriod" value="1M"/>
				  <input type="submit" id="1-year-btn" class="btn btn-secondary" name="timePeriod" value="1Y"/>
				</div>
				<div>
				SNP500 
				<input id="snpcheck" onChange="this.form.submit()" type="checkbox" name="SNP500" value="1" <%if(session.getAttribute("snp")!=null){%> <%="checked"%> <% } %>/>
				<input  type="hidden" name="SNP500" value="0"/>
				</div>
				</form>
	    		<div class = "graph-main">
	    		<div class="canvas-container">
	    			<canvas id="myChart" ></canvas>
	    		</div>
				<div class="container-header">
	    			<div class="add-stocks-container">
	    			
	    				<button class="button" id="customrange-button">Custom Range</button>
	    				<div class="modal" id="customrange-modal">
	    					<div class="modal-box">
	    						<div class="popup-header">Custom Date Range</div>
	    						<div class="popup-section">
	    							<form id="customrange-form" name="customrange-form" method="post" action="/api/GraphButtons">
	    								<div class="form-row">
	    									<label for="date-purchased">Start Date</label>
	    									<input type="text" class="datepicker" id="date-purchased" placeholder="MM/DD/YYY" name="date-purchased">
	    								</div>
	    								<div class="form-row">
	    									<label for="date-sold">End Date</label>
	    									<input type="text" class="datepicker" id="date-sold" placeholder="MM/DD/YYY" name="date-sold">
	    								</div>
	    								<div class="form-row">
	    									<span class="error-msg">${customrangeerrorMessage}</span>
	    								</div>
	    								<button type="submit" class="button" id="customrange-submit">Submit</button>
	    							</form>
	    						</div>
	    						<div class="popup-section">
	    							<button class="button" id="customrange-cancel">Cancel</button>
	    						</div>
	    					</div>
	    				</div>
	    				
	    			</div>
	    		</div> <!-- .container-header -->
	    		</div>
	    		</div> <!-- #graph-container -->
	    	
	    	<div class="grid-helper">
	    	<div class="homepage-container" id="portfolio-container">
	    		<div class="container-header">
	    			Your Portfolio
	    			<div class="add-stocks-container">
	    			
	    				<button class="button" id="add-stock-button">Add Stock</button>
	    				<div class="modal" id="add-stock-modal">
	    					<div class="modal-box">
	    						<div class="popup-header">Add Stock</div>
	    						<div class="popup-section">
	    							<form id="add-stock-form" name="addStock" method="post" action="/api/addstock" autocomplete="off">
	    								<div class="form-row">
	    									<label for="add-stock-ticker">Stock Ticker</label>
	    									<input type="text" class="ticker" id="add-stock-ticker" name="ticker">
	    								</div>
	    								<span class="error-msg">${errorMessageTicker}</span>
	    								<div class="form-row">
	    									<label for="add-stock-shares"># of Shares</label>
	    									<input type="number" id="add-stock-shares" name="shares">
	    								</div>
	    								<span class="error-msg">${errorMessageShares}</span>
	    								<div class="form-row">
	    									<label for="date-purchased">Date Purchased</label>
	    									<input type="text" class="datepicker" id="date-purchased-portfolio" placeholder="MM/DD/YYY" name="date-purchased">
	    								</div>
	    								<span class="error-msg">${errorMessageDatePurchased}</span>
	    								<div class="form-row">
	    									<label for="date-sold">Date Sold</label>
	    									<input type="text" class="datepicker" id="date-sold-portfolio" placeholder="MM/DD/YYY" name="date-sold">
	    								</div>
	    								<span class="error-msg">${errorMessageDateSold}</span>	    								
	    								<button type="submit" class="button" id="add-stock-submit">Add Stock</button>
	    							</form>
	    						</div>
	    						<div class="popup-section">
	    							<button class="button" id="add-stock-cancel">Cancel</button>
	    						</div>
	    					</div>
	    				</div>
	    				
	    				<button class="button" id="import-stock-button">Import CSV</button>
	    				<div class="modal" id="import-stock-modal">
	    					<div class="modal-box">
	    						<div class="popup-header">Import Stocks</div>
	    						<div class="popup-section">
	    							<form action="/api/csvimport" method="post" id="import-stock-form" enctype="multipart/form-data">
	    								<div class="form-row">
	    									<label for=""csvImport"">Upload a .csv file</label>
	    									<input type="file" name="file" id="csvImport" accept=".csv" required>
	    								</div>
	    								<div class="csvError">
	    									<span class="error-msg">${csvErrorMessage}</span>
	    								</div>
	    								<button type="submit" class="button" id="import-stock-submit">Upload File</button>
	    							</form>
	    						</div>
	    						<div class="popup-section">
	    							<button class="button" id="import-stock-cancel">Cancel</button>
	    						</div>
	    					</div>
	    				</div>
	    				
	    			</div>
	    		</div> <!-- .container-header -->
	    		
	    		<table id="stock-list">
	    		     
	    		     <% for(Stock stock : stockList) { %>
				        <tr>      
				            <td><%=stock.getName()%></td>
				            <td><%=stock.getTicker()%></td>
				            <td>
	    					<label class="switch" onclick="window.location='/api/toggleStock?ticker=<%=stock.getTicker()%>&type=owned'">
	    						<% if (stockToGraphMap.get(stock)) { %>
	    							<input type="checkbox" checked>
	    						<% } %>
	    						<% if (!stockToGraphMap.get(stock)) { %>
	    							<input type="checkbox">
	    						<% } %>
							  	<span class="slider round"></span>
							</label>
						</td>
	    				<td><a data-toggle="confirmation" data-title="Are you sure?" data-content="You cannot undo this action" data-btn-ok-label="Delete Stock" datta-btn-ok-class="btn-danger" data-btn-cancel-label="Cancel" href="/api/removestock?ticker=<%=stock.getTicker()%>&selector=portfolio"><i class="fas fa-trash"></i></a></td>
				        </tr>
				        
				    <% } %>
	    		</table>
	    		
	    		<div class="toggle-buttons">
	    			<a href="/api/toggleStock?type=selectAll" class="button toggle-button" id="select-all"><i class="fas fa-toggle-on"></i> &nbspSelect All</a>
	    			<a href="/api/toggleStock?type=deSelectAll" class="button toggle-button" id="deselect-all"><i class="fas fa-toggle-off"></i> &nbspDeselect All</a>
	    		</div> <!-- .toggle-buttons -->
	    		
	    	</div>  <!-- .homepage-container -->
	    	</div> <!-- .grid-helper -->
	    	
	    	<div class="grid-helper">
	    	<div class="homepage-container" id="viewed-container">
	    		<div class="container-header">
	    			Viewed Stocks
	    			<div class="add-stocks-container">
	    			
	    				<button class="button" id="view-stock-button">View Stock</button>
	    				<div class="modal" id="view-stock-modal">
	    					<div class="modal-box">
	    						<div class="popup-header">View Stock</div>
	    						<div class="popup-section">
	    							<form id="view-stock-form" name="viewStock" method="post" action="/api/viewstock" autocomplete="off">
	    								<div class="form-row">
	    									<label for="view-stock ticker">Stock Ticker</label>
	    									<input type="text" class="ticker" id="view-stock-ticker" name="ticker">
	    								</div>
	    								<span class="error-msg">${viewedErrorMessageTicker}</span>
	    								<div class="form-row">
	    									<label for="view-stock-shares"># of Shares</label>
	    									<input type="number" id="view-stock-shares" name="shares">
	    								</div>
	    								<span class="error-msg">${viewedErrorMessageShares}</span>
	    								<div class="form-row">
	    									<label for="date-purchased">Date Purchased</label>
	    									<input type="text" class="datepicker" id="date-purchased-viewed" placeholder="MM/DD/YYY" name="date-purchased">
	    								</div>
	    								<span class="error-msg">${viewedErrorMessageDatePurchased}</span>
	    								<div class="form-row">
	    									<label for="date-sold">Date Sold</label>
	    									<input type="text" class="datepicker" id="date-sold-viewed" placeholder="MM/DD/YYY" name="date-sold">
	    								</div>
	    								<span class="error-msg">${viewedErrorMessageDateSold}</span>	
	    								<button type="submit" class="button" id="view-stock-submit">View Stock</button>
	    							</form>
	    						</div>
	    						<div class="popup-section">
	    							<button class="button" id="view-stock-cancel">Cancel</button>
	    						</div>
	    					</div>
	    				</div>
	    				
	    			</div>
	    		</div> <!-- .container-header -->
	    		
	    		<!-- table for view stock -->
	    		<table id="stock-list">
	    		    <% for(Stock stock : viewedMap.keySet()) { %>
				        <tr>      
				            <td><%=stock.getName()%></td>
				            <td><%=stock.getTicker()%></td>
				            <td>
	    					<label class="switch" onclick="window.location='/api/toggleStock?ticker=<%=stock.getTicker()%>&type=viewed'">
	    						<% if (viewedMap.get(stock)) { %>
	    							<input type="checkbox" checked>
	    						<% } %>
	    						<% if (!viewedMap.get(stock)) { %>
	    							<input type="checkbox">
	    						<% } %>
							  	<span class="slider round"></span>
							</label>
						</td>
						<td><a data-toggle="confirmation" data-title="Are you sure?" data-content="You cannot undo this action" data-btn-ok-label="Delete Stock" datta-btn-ok-class="btn-danger" data-btn-cancel-label="Cancel" href="/api/removestock?ticker=<%=stock.getTicker()%>&selector=viewed"><i class="fas fa-trash"></i></a></td>
				        </tr>
				        
				    <% } %>
	    			
	    		</table>
	    	</div>  <!-- .homepage-container -->
	    	</div> <!-- .grid-helper -->
    	</div>
    </div>

    
    <div id='hiddendiv' name='hiddendiv' style='display:none;'>
    
  <!-- toggle button -->
  <script>
      toggleInvoke = (event) => {
        // console.log('go to event');
        let arg1 = event.target.getAttribute('data-arg1');
          // let arg1 = event.target.getAttribute('data-arg1');
          window.location='api/toggleStock?ticker' + arg1;
      }
  </script>

  <script>
    document.querySelector("#signOutB").onclick = function() {
      let httpRequest = new XMLHttpRequest();
    httpRequest.open("POST", "api/sio" , true);


    // We will get alerted when backend gives back some kind of response
    httpRequest.onreadystatechange = function(){
      // This function runs when we get some kind of response back from iTunes
      console.log(httpRequest);
      // When we get back a DONE state (readyState == 4, let's do something with it)
      if(httpRequest.readyState == httpRequest.DONE) {
        // Check for errors - status code 200 means success
        if(httpRequest.status == 200) {
          console.log(httpRequest.responseText);

          // Display the results on the browser - a separate function is created for this purpose
          window.location.href = "signIn.jsp";

        }
        else {
          console.log("AJAX Error!!");
          console.log(httpRequest.status);
          console.log(httpRequest.statusText);
        }

      }
    }
    httpRequest.send(); 

    }
  </script>
    <!-- Add stock popup box -->
	<script>
		var addStockModal = document.getElementById("add-stock-modal");
		var addStockButton = document.getElementById("add-stock-button");
		var addStockCancelButton = document.getElementById("add-stock-cancel");
		var addTickerError = '${errorMessageTicker}';
		var addSharesError = '${errorMessageShares}';
		var addSoldDateError = '${errorMessageDateSold}';
		var addPurchaseDateError = '${errorMessageDatePurchased}';
		
		// console.log("TESTING" + addTickerError);
		
		// When user clicks add stock button
		addStockButton.onclick = function() {
			document.getElementById("add-stock-form").reset();
			addStockModal.style.display = "flex";
		}
		// When user cancels adding a stock
		addStockCancelButton.onclick = function() {
			addStockModal.style.display = "none";
		}
		// When the user clicks anywhere outside of the box, close it
		window.onclick = function(event) {
			if (event.target == addStockModal) {
				addStockModal.style.display = "none";
			} else if (event.target == importStockModal) {
				importStockModal.style.display = "none";
			} else if (event.target == viewStockModal) {
				viewStockModal.style.display = "none";
			}else if (event.target == Modal) {
				Modal.style.display = "none";
			}
		}
		// If the servlet returns an error message, display popup
		if(addTickerError != "" || addSharesError != "" || addSoldDateError != "" || addPurchaseDateError != "") {
			addStockModal.style.display = "flex";
		}
	</script>
	
	<!-- Import stocks popup box -->
	<script>
		var importStockModal = document.getElementById("import-stock-modal");
		var importStockButton = document.getElementById("import-stock-button");
		var importStockCancelButton = document.getElementById("import-stock-cancel");
		var csvErrorMessage = '${csvErrorMessage}';
		
		// When user clicks add stock button
		importStockButton.onclick = function() {
			document.getElementById("import-stock-form").reset();
			importStockModal.style.display = "flex";
		}
		// When user cancels adding a stock
		importStockCancelButton.onclick = function() {
			importStockModal.style.display = "none";
		}
		
		if(csvErrorMessage != "") {
			importStockModal.style.display = "flex";
		}
	</script>
	
	<!-- Custom Range popup box -->
	<script>
		var Modal = document.getElementById("customrange-modal");
		var Button = document.getElementById("customrange-button");
		var CancelButton = document.getElementById("customrange-cancel");
		var CusterrorMessage = '${customrangeerrorMessage}';
		
		// When user clicks add stock button
		Button.onclick = function() {
			document.getElementById("customrange-form").reset();
			Modal.style.display = "flex";
		}
		// When user cancels adding a stock
		CancelButton.onclick = function() {
			Modal.style.display = "none";
		}
		// When the user clicks anywhere outside of the box, close it
		
		// If the servlet returns an error message, display popup
		if(CusterrorMessage != "") {
			console.log("errorMessage = " + CusterrorMessage);
			Modal.style.display = "flex";
		}
	</script>
	
	
	<!-- View stocks popup box -->
	<script>
		var viewStockModal = document.getElementById("view-stock-modal");
		var viewStockButton = document.getElementById("view-stock-button");
		var viewStockCancelButton = document.getElementById("view-stock-cancel");
		var tickerError = '${viewedErrorMessageTicker}';
		var sharesError = '${viewedErrorMessageShares}';
		var soldDateError = '${viewedErrorMessageDateSold}';
		var purchaseDateError = '${viewedErrorMessageDatePurchased}';
		
		// When user clicks add stock button
		viewStockButton.onclick = function() {
			document.getElementById("view-stock-form").reset();
			viewStockModal.style.display = "flex";
		}
		// When user cancels adding a stock
		viewStockCancelButton.onclick = function() {
			viewStockModal.style.display = "none";
		}
		// If the servlet returns an error message, display popup
		if(tickerError != "" || sharesError != "" || soldDateError != "" || purchaseDateError != "") {
			viewStockModal.style.display = "flex";
		}
	</script>
	

	<!-- graph script, main idea and getting data from session done -->
	<script>
	
	// Graph variables
	var isGraph = '<%= isGraph %>'
	var graphdata = <%= GraphData %>
	var labels = <%= Labels %>
    
	var tester = JSON.parse(graphdata[0]);            
	var apple_from_javafile_output = {"borderColor":["rgba(120,0,114, 1)"],"data":[66.809997558594,73.410003662109,77.379997253418,68.339996337891,63.569999694824,73.449996948242,79.480003356934,91.199996948242,106.26000213623,129.03999328613,115.80999755859,117.51000213623],"borderWidth":1,"label":"Apple Inc value in $","fill":"false"}
	console.log(tester)
	console.log (apple_from_javafile_output)
   
	// Graph options
	var config = {
		type: 'line',
	    data: {
	        labels: [],
	        datasets: []
	    },
	    options: {
	    	responsive: true,
	    	maintainAspectRatio: false,
	        scales: {
	            yAxes: [{
	                ticks: {
	                    beginAtZero: false
	                }
	            }]
	        },
	    plugins: {
            zoom: {
                // Container for pan options
                pan: {
                    // Boolean to enable panning
                    enabled: true,

                    // Panning directions. Remove the appropriate direction to disable 
                    // Eg. 'y' would only allow panning in the y direction
                    mode: 'xy'
                },

                // Container for zoom options
                zoom: {
                    // Boolean to enable zooming
                    enabled: true,

                    // Zooming directions. Remove the appropriate direction to disable 
                    // Eg. 'y' would only allow zooming in the y direction
                    mode: 'xy',
                }
            }
        }
  	}
	}
  
	for(var i=0; i<graphdata.length; i++) {
		config.data.datasets.push(JSON.parse(graphdata[i]));	
	}
	
	config.data.labels = labels
	
	var ctx = document.getElementById('myChart');
	var myChart = new Chart(ctx, config); 
	
	var hdn = JSON.stringify(graphdata);
	document.getElementById("hiddendiv").innerHTML = hdn;
	console.log(hdn);
	
	$('#zoomin').click(function(){
	    
		console.log("Zoom in ")
		
	    var evt = document.createEvent('MouseEvents');
	    evt.initEvent('wheel', true, true); evt.deltaY = -1000; 
	    document.getElementById("myChart").dispatchEvent(evt);
	    document.getElementById("myChart").dispatchEvent(evt);

	});
	
$('#zoomout').click(function(){
	    
	console.log("Zoom out ")
	
	    var evt = document.createEvent('MouseEvents');
	    evt.initEvent('wheel', true, true); evt.deltaY = 1000; 
	    document.getElementById("myChart").dispatchEvent(evt);
	    document.getElementById("myChart").dispatchEvent(evt);

	});
			
	</script>
	
		<script>
		$('[data-toggle=confirmation]').confirmation({
		  rootSelector: '[data-toggle=confirmation]',
		  // other options
		});
		</script>
	
</body>
</html>
