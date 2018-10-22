var app= angular.module('crudApp',[]);  //,['ui.bootstrap']
//var app= angular.module('crudApp',['ngProgress','ui.bootstrap']);

app.config(['$httpProvider', function ($httpProvider) {
  //Reset headers to avoid OPTIONS request (aka preflight)
  $httpProvider.defaults.headers.common = {};
  $httpProvider.defaults.headers.post = {};
  $httpProvider.defaults.headers.put = {};
  $httpProvider.defaults.headers.patch = {};
}]);

		
app.service('SongService', function ($http) {
	
    $http.defaults.headers.post["Content-Type"] = "text/plain";

    this.getSingers = function getSingers() {
        return $http({
            method : 'GET',
            url : 'http://localhost:8080/song/singers'			
        });
    }	
	
	this.getSongs = function getSongs() {
        return $http({
            method : 'GET',
            url : 'http://localhost:8080/song/listSong'			
        });
    }	
	
	this.addSong = function addSong(Song) {		
    return $http({		
        method : 'POST',
        url : 'http://localhost:8080/song/entrySong',
		headers: { 
        'Accept': 'application/json',
        'Content-Type': 'application/json' 
    },
        data : {
            title: Song.title,
            film: Song.film,
			year: Song.year,
            singer:Song.songList
        }
    });
   }
   
 this.searchSong = function searchSong(Song) {
	if(Song.year !== null && Song.year !== "" && !angular.isUndefined(Song.year)) {	
		return $http({		
			method : 'GET',
			url : 'http://localhost:8080/song/songYear', 
			headers: { 
			'Accept': 'application/json',
			'Content-Type': 'application/json' 
			},
			params : {           
				year: Song.year
			   
			}
		} );
     }
	 
	else if(Song.singer !== null && Song.singer !== "" && !angular.isUndefined(Song.singer)) {	
		return $http({		
			method : 'GET',
			url : 'http://localhost:8080/song/songSinger', 
			headers: { 
			'Accept': 'application/json',
			'Content-Type': 'application/json' 
			},
			params : {           
				name: Song.singer			   
			}
		} );
     }
   }   
   
   this.editSong = function editSong(Song) {	
		return $http({		
			method : 'PUT',
			url : 'http://localhost:8080/song/songUpt', 
			headers: { 
			'Accept': 'application/json',
			'Content-Type': 'application/json' 
			},
			params : {           
				title: Song.title,
				film: Song.film,
				year: Song.year,
				singer:Song.songList			   
			}
		} );
   }
   
});			
	
app.controller("SongController", function ($scope, $window, SongService) {	  	
	$scope.buttonText="Submit";
	$scope.buttonText2="Search";
 
    $scope.GetListOfSinger = function() {
                var message = "";				
				 message += $scope.newsong.singer;				  
				$scope.newsong.songList = message;
             //   $window.alert(message);
    }


    $scope.saveSong = function() {    
	if(angular.isUndefined( $scope.newsong.id)){
		$scope.GetListOfSinger(); 
	   // $scope.newsong.push($scope.newsong.singer);
		SongService.addSong($scope.newsong)
           .then (function success(response){
              $scope.message = 'Song added!';
              $scope.errorMessage = '';
          },
          function error(response){
              $scope.errorMessage = 'Error adding song!';
              $scope.message = '';
        });   
        //clear the add contact form
        $scope.newsong = {};
	}	
	else {
		SongService.editSong($scope.newsong)
		     .then (function success(response){
			  $scope.message = 'Song Updated!';
              $scope.errorMessage = '';
          },
          function error(response){
              $scope.errorMessage = 'Error updating song!';
              $scope.message = '';
        });   
        //clear the add contact form
        $scope.newsong = {};
				 
	}
 }
 
 $scope.searchSong = function() {    
   SongService.searchSong($scope.newsong)
				 .then (function success(response){
					 
			  $scope.songArr = response.data;
			  $scope.songs = $scope.songArr[0];	
			  $scope.songArrSize = response.data.length;			 
              $scope.message = 'Song Search!';
              $scope.errorMessage = '';
          },
          function error(response){
              $scope.errorMessage = 'Searching err!';
              $scope.message = '';
        }); 
 }
	
	
	$scope.delete = function(id) {        
        //search contact with given id and delete it
        for(i in $scope.songs) {
            if($scope.songs[i].id == id) {
                $scope.songs.splice(i,1);
                $scope.newsong = {};
            }
        }        
    }
	
	$scope.edit = function(id) {
		$scope.buttonText="Edit";
	//search contact with given id and update it
        for(i in $scope.songs) {
            if($scope.songs[i].id == id) {
            	//we use angular.copy() method to create 
            	//copy of original object
                $scope.newsong = angular.copy($scope.songs[i]);				
					
            }
        }
    }	  
	
	$scope.numOfPages = function(index) { 	
			return $scope.songs = $scope.songArr[index];   
        };
	
	SongService.getSingers()
      .then(function success(response) {
          $scope.singers = response.data;
          $scope.message='';
          $scope.errorMessage = '';
      },
      function error (response) {
          $scope.message='';
          $scope.errorMessage = 'Error getting singers!';
      });
	  
	SongService.getSongs()
      .then(function success(response) {
          $scope.songArr = response.data;
		  $scope.songs = $scope.songArr[0];	
		  $scope.songArrSize = response.data.length;			
          $scope.message='';
          $scope.errorMessage = '';
      },
      function error (response) {
          $scope.message='';
          $scope.errorMessage = 'Error getting song list!';
		  
      } );
	  
	  
	
	  
	
  });  

			
	 

  