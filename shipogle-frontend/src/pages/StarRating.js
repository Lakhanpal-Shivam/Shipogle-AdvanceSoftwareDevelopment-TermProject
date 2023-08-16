import * as React from "react";
import Box from "@mui/material/Box";
import { styled } from "@mui/material/styles";
import Rating from "@mui/material/Rating";

const StyledRating = styled(Rating)({
  "& .MuiRating-iconFilled": {
    color: "#FFD700",
  },
  "& .MuiRating-iconHover": {
    color: "#FFD700",
  },
});

export default function BasicRating({ starRating }) {
  const [value, setValue] = React.useState(0);

  return (
    <Box
      sx={{
        "& > legend": { mt: 2 },
      }}
    >
      <StyledRating
        name="simple-controlled"
        size="large"
        value={value}
        precision={0.5}
        //https://stackoverflow.com/questions/69448789/how-to-enlarge-material-ui-rating-icons#:~:text=To%20increase%20the%20size%20of,the%20container%20they're%20in.&text=will%20increase%20the%20size%20of,set%20size%3D%22large%22%20.
        sx={{
          fontSize: "4rem",
        }}
        onChange={(event, newValue) => {
          setValue(newValue);
          starRating(event.target.value);
        }}
      />
    </Box>
  );
}
