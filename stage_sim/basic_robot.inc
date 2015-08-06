
define topurg ranger
(
	sensor( 			
		range [ 0.0  3.0 ]
		fov 135.0
		samples 1081
	)

  # generic model properties
  color "black"
  size [ 0.05 0.05 0.1 ]
)

define cleaner_robot position
(
   size [1.5 1.5 1]
   # the shape of cleaner_robot
	block
	(
		points 6
		point[5] [0 0]
		point[4] [0 1]
		point[3] [0.75 1]
		point[2] [1 0.75]
		point[1] [1 0.25]
		point[0] [0.75 0]
		z [0 1]
	)
	block
	(
		points 4
		point[3] [1 0.75]
		point[2] [1.25 0.75]
		point[1] [1.25 0.625]
		point[0] [1 0.625]
		z [0 0.5]
	)
	block
	(
		points 4
		point[3] [1 0.375]
		point[2] [1.25 0.375]
		point[1] [1.25 0.25]
		point[0] [1 0.25]
		z [0 0.5]
	)
	
	topurg(pose [ 0.050 0.000 -0.5 0.000 ])
	
	obstacle_return 1
	laser_return 1
	ranger_return 1
	blob_return 1
)
