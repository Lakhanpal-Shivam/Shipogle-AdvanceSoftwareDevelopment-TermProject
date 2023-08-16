import * as React from "react";

import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import Grid from "@mui/material/Grid";
import Typography from "@mui/material/Typography";
import { debounce } from "@mui/material/utils";

const autocompleteService = { current: null };

export default function LocationAutoComplete({
  id,
  setSelectedLocation,
  fieldType,
  label,
  placeholder,
  required,
}) {
  const [value, setValue] = React.useState(null);
  const [inputValue, setInputValue] = React.useState("");
  const [options, setOptions] = React.useState([]);

  const fetch = React.useMemo(
    () =>
      debounce((request, callback) => {
        autocompleteService.current.getPlacePredictions(request, callback);
      }, 400),
    []
  );

  React.useEffect(() => {
    let active = true;
    if (!autocompleteService.current && window.google) {
      autocompleteService.current =
        new window.google.maps.places.AutocompleteService();
    }
    if (!autocompleteService.current) {
      return undefined;
    }

    if (inputValue === "") {
      setOptions(value ? [value] : []);
      return undefined;
    }

    fetch({ input: inputValue }, (results) => {
      if (active) {
        let newOptions = [];

        if (value) {
          newOptions = [value];
        }

        if (results) {
          newOptions = [...newOptions, ...results];
        }

        setOptions(newOptions);
      }
    });

    return () => {
      active = false;
    };
  }, [value, inputValue, fetch]);

  return (
    <Autocomplete
      id={id ? id : "google-autocomplete"}
      sx={{
        flexGrow: 1,
        margin: "auto",
        minWidth: "200px !important",
        maxWidth: "300px",
        width: "100%",
      }}
      getOptionLabel={(option) =>
        typeof option === "string" ? option : option.description
      }
      filterOptions={(x) => x}
      options={options}
      autoComplete
      includeInputInList
      filterSelectedOptions
      value={value}
      noOptionsText="No locations"
      onChange={(event, newValue) => {
        setOptions(newValue ? [newValue, ...options] : options);
        setValue(newValue);
        setSelectedLocation(id, newValue);
      }}
      onInputChange={(event, newInputValue) => {
        setInputValue(newInputValue);
      }}
      renderInput={(params) => (
        <TextField
          variant={fieldType}
          {...params}
          label={label}
          required={required}
          placeholder={placeholder}
          fullWidth
        />
      )}
      renderOption={(props, option) => {
        const suggestion = option.description.split(",");
        const main = suggestion[0];
        let secondary = "";
        for (let i = 1; i < suggestion.length; i++) {
          secondary += suggestion[i];
          if (i < suggestion.length - 1) {
            secondary += ", ";
          }
        }
        return (
          <li {...props}>
            <Grid container alignItems="center">
              <Grid item sx={{ display: "flex", width: 44 }}>
                <LocationOnIcon sx={{ color: "text.secondary" }} />
              </Grid>
              <Grid
                item
                sx={{ width: "calc(100% - 44px)", wordWrap: "break-word" }}
              >
                <Typography variant="body1" color="text.primary">
                  {main}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {secondary}
                </Typography>
              </Grid>
            </Grid>
          </li>
        );
      }}
    />
  );
}
