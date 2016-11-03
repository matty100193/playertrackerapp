<?php
          $con=mysqli_connect("localhost","matchr3_prem16","matty100","matchr3_prem16");
          
             $currentDate=getdate();
             $result = mysqli_prepare($con, "SELECT Results.*,Lineup.* FROM Results Left Join Lineup ON Results.lineup_id=Lineup.id ORDER BY Results.round"); 
             mysqli_stmt_execute($result);
             $result = mysqli_stmt_get_result($result);
        

          $results = array();
          
          while($row =mysqli_fetch_assoc($result)){
          	$results[]=$row;
          }
          
          echo json_encode($results);
          mysqli_close($con);
          
?>