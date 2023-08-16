import {
  Button,
  Checkbox,
  FormControl,
  InputLabel,
  ListItemText,
  MenuItem,
  OutlinedInput,
  Select,
  TextField,
} from "@mui/material";
import * as React from "react";
import customAxios from "../utils/MyAxios";
import Constants from "../Constants";
import CommonFunctions from "../services/CommonFunction";

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
  PaperProps: {
    style: {
      maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
      width: 250,
    },
  },
};

export default function CreatePackage({ routeDetails, packageCreated }) {
  const commFunc = new CommonFunctions();
  const [packages, setPackages] = React.useState(1);
  const [maxLength, setMaxLength] = React.useState(1);
  const [maxWidth, setMaxWidth] = React.useState(1);
  const [maxHeight, setMaxHeight] = React.useState(1);
  const [askPrice, setAskPrice] = React.useState(routeDetails.price);
  const [packageDescription, setDescription] = React.useState("");
  const allowedCategoryLabels = routeDetails.allowedCategory;
  const [Category, setCategory] = React.useState([]);
  const onSubmit = (event) => {
    event.preventDefault();
    const body = {
      width: maxWidth,
      height: maxHeight,
      length: maxLength,
      title: "Package request by " + window.localStorage.getItem("user_name"),
      description: packageDescription,
      pickup_address: routeDetails.sourceCity,
      drop_address: routeDetails.destinationCity,
    };
    customAxios.post(Constants.CREATEPACKAGE, body).then(
      (res) => {
        packageCreated(res.data);
      },
      (error) => {
        console.error(error);
        commFunc.showAlertMessage(
          "error while creating packages",
          "error",
          3000,
          "bottom"
        );
      }
    );
  };
  return (
    <>
      <div className="package-form-container">
        <h3 style={{ margin: "1.5rem 2rem" }}>Enter your Package Details</h3>
        <form
          id="courierForm"
          onSubmit={(event) => {
            onSubmit(event);
          }}
        >
          <TextField
            className="courier-form-input"
            id="number-of-packages"
            type="number"
            label="Packages"
            min="1"
            required
            placeholder="1"
            value={packages}
            onChange={(event) => {
              const value = event.target.value;
              setPackages(value);
            }}
          ></TextField>
          <div
            style={{
              display: "flex",
              minWidth: 200,
              maxWidth: 300,
              width: "100%",
            }}
          >
            <TextField
              id="package-length"
              variant="filled"
              type="number"
              label="Length (in cm)"
              placeholder="10"
              min="1"
              required
              sx={{ flexGrow: 1 }}
              value={maxLength}
              onChange={(event) => {
                const value = event.target.value;
                setMaxLength(value);
              }}
            ></TextField>
            &nbsp;
            <TextField
              id="package-width"
              variant="filled"
              type="number"
              min="1"
              required
              label="Width (in cm)"
              placeholder="10"
              sx={{ flexGrow: 1 }}
              value={maxWidth}
              onChange={(event) => {
                const value = event.target.value;
                setMaxWidth(value);
              }}
            ></TextField>
            &nbsp;
            <TextField
              id="package-height"
              variant="filled"
              min="1"
              required
              type="number"
              label="Height (in cm)"
              placeholder="10"
              sx={{ flexGrow: 1 }}
              value={maxHeight}
              onChange={(event) => {
                const value = event.target.value;
                setMaxHeight(value);
              }}
            ></TextField>
          </div>
          <div
            style={{
              display: "flex",
              maxWidth: "300px",
              width: "100%",
              padding: "0px 16px",
            }}
          >
            <TextField
              id="price"
              sx={{
                flexGrow: 1,
              }}
              label="Ask Price (CAD)"
              required
              type="number"
              min="1"
              value={askPrice}
              onChange={(event) => {
                const value = event.target.value;
                setAskPrice(value);
              }}
            ></TextField>
            &nbsp;
          </div>
          <FormControl sx={{ m: 1, maxWidth: "300px", width: "100%" }}>
            <InputLabel id="allowedCategoryLabel">Type of Package</InputLabel>
            <Select
              required
              labelId="allowedCategoryLabel"
              id="allowedCategory"
              multiple
              value={Category}
              onChange={(event) => {
                const {
                  target: { value },
                } = event;
                setCategory(
                  typeof value === "string" ? value.split(",") : value
                );
              }}
              input={<OutlinedInput required label="Category" />}
              renderValue={(selected) => selected.join(", ")}
              MenuProps={MenuProps}
            >
              {allowedCategoryLabels.map((cateogory) => (
                <MenuItem key={cateogory} value={cateogory}>
                  <Checkbox checked={Category.indexOf(cateogory) > -1} />
                  <ListItemText primary={cateogory} />
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <TextField
            id="package-description"
            label="Description"
            sx={{ gridColumnEnd: "span 2", width: "93%" }}
            value={packageDescription}
            required
            multiline
            maxRows={3}
            onChange={(event) => {
              setDescription(event.target.value);
            }}
          ></TextField>
          <Button
            type="submit"
            variant="contained"
            style={{
              margin: "0.5rem auto 1rem auto",
              gridColumnEnd: "span 2",
              minWidth: "180px",
              maxWidth: "300px",
              width: "100%",
            }}
          >
            Submit
          </Button>
        </form>
      </div>
    </>
  );
}
