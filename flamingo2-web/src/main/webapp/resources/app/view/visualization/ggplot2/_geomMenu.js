/*
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
Ext.define('Flamingo2.view.visualization.ggplot2._geomMenu', {
    extend: 'Ext.menu.Menu',
    alias: 'widget.ggplot2GeomMenu',

    requires: [
        'Flamingo2.view.visualization.ggplot2._geomMenuController'
    ],
    controller: 'ggplot2GeomMenuController',
    items: [
        '<b class="menu-title"></b>',
        {
            text: "Univariate Geoms",
            iconCls: 'add',
            hidden: true,
            menu: [
                {
                    "text": "histogram",
                    "iconCls": "ggplot_icon_histogram",
                    "geom": "histogram",
                    "aes": ["binwidth", "colour", "fill", "size", "linetype", "weight", "alpha", "position"]
                },
                {
                    "text": "density",
                    "iconCls": "ggplot_icon_density",
                    "geom": "density",
                    "aes": ["kernel", "adjust", "fill", "weight", "colour", "alpha", "size", "linetype"]
                },
                {
                    "text": "freqpoly",
                    "iconCls": "ggplot_icon_freqpoly",
                    "geom": "freqpoly",
                    "aes": ["binwidth", "colour", "size", "linetype", "alpha"]
                }
            ]
        },
        {
            text: "Bivariate Geoms",
            iconCls: 'add',
            hidden: true,
            menu: [
                {
                    "text": "area",
                    "iconCls": "ggplot_icon_area",
                    "geom": "area",
                    "aes": ["colour", "fill", "size", "linetype", "alpha"]
                },
                {
                    "text": "bar",
                    "iconCls": "ggplot_icon_bar",
                    "geom": "bar",
                    "aes": ["colour", "fill", "size", "linetype", "weight", "alpha", "position"]
                },
                {
                    "text": "bin2d",
                    "iconCls": "ggplot_icon_bin2d",
                    "geom": "bin2d",
                    "aes": ["bins", "colour", "fill", "size", "linetype", "weight", "alpha"]
                },
                {
                    "text": "boxplot",
                    "iconCls": "ggplot_icon_boxplot",
                    "geom": "boxplot",
                    "aes": ["weight", "colour", "fill", "size", "alpha"]
                },
                {
                    "text": "contour",
                    "iconCls": "ggplot_icon_contour",
                    "geom": "contour",
                    "aes": ["z", "binwidth", "bins", "weight", "colour", "size", "linetype", "alpha"]
                },
                {
                    "text": "density2d",
                    "iconCls": "ggplot_icon_density2d",
                    "geom": "density2d",
                    "aes": ["bins", "colour", "size", "linetype", "alpha"]
                },
                {
                    "text": "hex",
                    "iconCls": "ggplot_icon_hex",
                    "geom": "hex",
                    "aes": ["bins", "colour", "fill", "size", "alpha"]
                },
                {
                    "text": "path",
                    "iconCls": "ggplot_icon_path",
                    "geom": "path",
                    "aes": ["colour", "size", "linetype", "alpha"]
                },
                {
                    "text": "polygon",
                    "iconCls": "ggplot_icon_polygon",
                    "geom": "polygon",
                    "aes": ["colour", "fill", "size", "linetype", "alpha"]
                },
                {
                    "text": "point",
                    "iconCls": "ggplot_icon_point",
                    "geom": "point",
                    "aes": ["shape", "colour", "size", "fill", "alpha", "position"]
                },
                {
                    "text": "quantile",
                    "iconCls": "ggplot_icon_quantile",
                    "geom": "quantile",
                    "aes": ["number", "colour", "size", "linetype", "alpha"]
                },
                {
                    "text": "rug",
                    "iconCls": "ggplot_icon_rug",
                    "geom": "rug",
                    "aes": ["colour", "size", "linetype", "alpha", "position"]
                },
                {
                    "text": "smooth",
                    "iconCls": "ggplot_icon_smooth",
                    "geom": "smooth",
                    "aes": ["method", "se", "colour", "fill", "size", "linetype", "weight", "alpha"]
                },
                {
                    "text": "step",
                    "iconCls": "ggplot_icon_step",
                    "geom": "step",
                    "aes": ["direction", "colour", "size", "linetype", "alpha"]
                },
                {
                    "text": "text",
                    "iconCls": "ggplot_icon_text",
                    "geom": "text",
                    "aes": ["label", "colour", "size", "angle", "alpha"]
                },
                {
                    "text": "tile",
                    "iconCls": "ggplot_icon_tile",
                    "geom": "tile",
                    "aes": ["fill", "colour", "size", "linetype", "alpha"]
                }
            ]
        },
        {
            text: "fixed lines/bars",
            iconCls: 'add',
            hidden: true,
            menu: [
                {
                    "text": "abline",
                    "iconCls": "ggplot_icon_abline",
                    "geom": "abline",
                    "aes": ["intercept", "slope", "colour", "size", "linetype", "alpha"]
                },
                {
                    "text": "hline",
                    "iconCls": "ggplot_icon_hline",
                    "geom": "hline",
                    "aes": ["yintercept", "colour", "size", "linetype", "alpha"]
                },
                {
                    "text": "vline",
                    "iconCls": "ggplot_icon_vline",
                    "geom": "vline",
                    "aes": ["xintercept", "colour", "size", "linetype", "alpha"]
                },
                {
                    "text": "crossbar",
                    "iconCls": "ggplot_icon_crossbar",
                    "geom": "crossbar",
                    "aes": ["ymin", "ymax", "colour", "fill", "size", "linetype", "alpha"]
                },
                {
                    "text": "errorbar",
                    "iconCls": "ggplot_icon_errorbar",
                    "geom": "errorbar",
                    "aes": ["ymin", "ymax", "colour", "size", "linetype", "width", "alpha"]
                },
                {
                    "text": "errorbarh",
                    "iconCls": "ggplot_icon_errorbarh",
                    "geom": "errorbarh",
                    "aes": ["xmin", "xmax", "colour", "size", "linetype", "width", "alpha"]
                },
                {
                    "text": "linerange",
                    "iconCls": "ggplot_icon_linerange",
                    "geom": "linerange",
                    "aes": ["ymin", "ymax", "colour", "size", "linetype", "alpha"]
                },
                {
                    "text": "pointrange",
                    "iconCls": "ggplot_icon_pointrange",
                    "geom": "pointrange",
                    "aes": ["ymin", "ymax", "colour", "size", "linetype", "shape", "alpha", "fill"]
                },
                {
                    "text": "ribbon",
                    "iconCls": "ggplot_icon_ribbon",
                    "geom": "ribbon",
                    "aes": ["ymin", "ymax", "colour", "fill", "size", "linetype", "alpha"]
                }
            ]
        }
    ]
});